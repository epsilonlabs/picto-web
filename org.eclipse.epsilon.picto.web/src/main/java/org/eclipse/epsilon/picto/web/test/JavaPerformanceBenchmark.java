/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web.test;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.InterfaceDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Modifier;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.java.emf.meta.JavaFactory;
import org.eclipse.gmt.modisco.java.emf.meta.JavaPackage;

import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * A test class that mocks http requests from web clients. For assertions, The
 * tests check the values and elements inside the returned view to examine their
 * correctness.
 * 
 * @author Alfa Yohannis
 *
 */
public class JavaPerformanceBenchmark {

  /**
   * 
   */
  private static String OTHER_LOG_FILE_NAME = "/var/tmp/picto-web.log";
  private static String MODEL_ORIGINAL = "/java/java.big.xmi";
  private static final String MODEL_TMP = "/java.xmi";
  private static final String MODEL = "/java/java.xmi";

  private static final File modelFile = new File(
      new File(PictoApplication.WORKSPACE + File.separator + MODEL).getAbsolutePath());
  private static final File modelFileTmp = new File(
      new File(PictoApplication.TEMP + File.separator + MODEL_TMP).getAbsolutePath());

  private static final XMIResource resource = new XMIResourceImpl(URI.createFileURI(modelFileTmp.getAbsolutePath()));

  static final Random random = new Random();
  static Set<String> clientWaitingList = Collections.synchronizedSet(new HashSet<>());

  static ObjectMapper mapper = new ObjectMapper();
  static Set<String> expectedViews;
  private static List<String> classNames = new ArrayList<>();
  private static File logFile;

  public static void main(String... args) throws Exception {

    long startTime = System.currentTimeMillis();

    logFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "picto-web.log");
    if (logFile.exists())
      logFile.delete();
    logFile.createNewFile();

    System.out.println("Dump output into file  " + logFile.getAbsolutePath());

    if (System.getProperty("os.name").contains("indows")) {
      runCommandSync("mvn.cmd -f pom-performance.xml clean install -Dmaven.test.skip=true");
    } else {
      runCommandSync("mvn -f pom-performance.xml clean install -Dmaven.test.skip=true");
    }

    File tempDir = new File(PictoApplication.TEMP);
    if (!tempDir.exists()) {
      tempDir.mkdir();
    }

    PerformanceRecorder.initFile(new File("data/selective.csv"));
    PerformanceRecorder.startRecording();
    PerformanceRecorder.header("num,genall,views,iteration,client,path,waittime,bytes,type,properties");

    final int numberOfMeasurementPoints = 5;

//     configuration for smaller experiment
//    int numberOfViews = 12; // Number of nodes the graph model
    int numberOfViews = 0; // Number of nodes the graph model

    int[] numbersOfAffectedViews = null;
    boolean[] genAllViews = { true, false };
//    boolean[] genAllViews = { false };

    /** comment this if we want to test using the big model */
//    numberOfClients = 1; // number of clients subscribed to Picto Web's STOMP server.
//    numberOfIteration = 3; // Number of iteration measuring for each number of affected views
//    MODEL_ORIGINAL = "/java/java.small.xmi";
//    MODEL_ORIGINAL = "/java/java.medium.xmi";
//    MODEL_ORIGINAL = "/java/java.xx.big.xmi";
//    MODEL_ORIGINAL = "/java/java.x.big.xmi";

    File modelFileOriginal = new File(PictoApplication.WORKSPACE + File.separator + MODEL_ORIGINAL);
    XMIResource resourceOriginal = new XMIResourceImpl(URI.createFileURI(modelFileOriginal.getAbsolutePath()));

    Map<Object, Object> loadOptions = ((XMIResourceImpl) resource).getDefaultLoadOptions();
    loadOptions.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

    Map<Object, Object> saveOptions = ((XMIResourceImpl) resource).getDefaultSaveOptions();
    saveOptions.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

    JavaPackage.eINSTANCE.eClass();
    System.out.print("Loading the original model ... ");
    resourceOriginal.load(loadOptions);
    System.out.println("Done");
    System.out.print("Copy the original model ... ");
    resource.getContents().addAll(EcoreUtil.copyAll(resourceOriginal.getContents()));
    System.out.println("Done");
    System.out.print("Unload the original model ... ");
    resourceOriginal.unload();
    System.out.println("Done");

    List<String> classList = new ArrayList<>();
    classNames.clear();
    TreeIterator<EObject> treeIterator = resource.getAllContents();
    while (treeIterator.hasNext()) {
      EObject eObject = treeIterator.next();
      if (eObject instanceof ClassDeclaration) {
        ClassDeclaration cd = (ClassDeclaration) eObject;

        // add a dummy field into every class to be edited later
        FieldDeclaration fd = JavaFactory.eINSTANCE.createFieldDeclaration();
        Modifier modifier = JavaFactory.eINSTANCE.createModifier();
        modifier.setVisibility(VisibilityKind.PRIVATE);
        fd.setModifier(modifier);
        TypeAccess typeAccess = JavaFactory.eINSTANCE.createTypeAccess();
        typeAccess.setType(JavaFactory.eINSTANCE.createPrimitiveTypeInt());
        fd.setType(typeAccess);
        VariableDeclarationFragment vd = JavaFactory.eINSTANCE.createVariableDeclarationFragment();
        vd.setName("dummy");
        fd.getFragments().add(0, (VariableDeclarationFragment) vd);
        cd.getBodyDeclarations().add(0, fd);
      }
    }

    int count = 0;
    treeIterator = resource.getAllContents();
    while (treeIterator.hasNext()) {
      EObject eObject = treeIterator.next();
      resource.setID(eObject, "e" + count);

      if (eObject instanceof ClassDeclaration) {
        ClassDeclaration cd = (ClassDeclaration) eObject;

        // construct names for paths
        EObject eContainer = cd.eContainer();
        List<String> segments = new ArrayList<>();
        segments.add(cd.getName());
        Model model = null;
        while (eContainer != null) {
          if (eContainer instanceof ClassDeclaration) {
            segments.add(0, ((ClassDeclaration) eContainer).getName());
          } else if (eContainer instanceof InterfaceDeclaration) {
            segments.add(0, ((InterfaceDeclaration) eContainer).getName());
          } else if (eContainer instanceof Package) {
            segments.add(0, ((Package) eContainer).getName());
          } else if (eContainer instanceof Model) {
            model = (Model) eContainer;
            segments.add(0, model.getName());
          }
          eContainer = eContainer.eContainer();
        }
        classNames.add(String.join(".", segments));
        String id = resource.getID(eObject);
        classList.add(id);
      }
      count++;
    }

    resource.save(saveOptions);

    PerformanceRecorder.globalNumberOfViews = numberOfViews;
    numberOfViews = classList.size();
    numbersOfAffectedViews = new int[numberOfMeasurementPoints + 1];

    // order from low to high
    for (int i = 0; i < numberOfMeasurementPoints; i++) {
      numbersOfAffectedViews[i + 1] = 0 + (numberOfViews / numberOfMeasurementPoints) * (i + 1);
    }
    numbersOfAffectedViews[0] = numbersOfAffectedViews[numbersOfAffectedViews.length - 1] - 1;

    System.out.println("Iteration = " + Arrays.toString(numbersOfAffectedViews));

    // modify
    try {

      for (boolean genAll : genAllViews) {
        PerformanceRecorder.genenerateAll = genAll;
        // iterate for each number of affected views
        for (int numViews : numbersOfAffectedViews) {
          PerformanceRecorder.globalNumberOfAffectedViews = numViews;

          // run this code if we want to run the performance benchmark on separate JVM
          // processes
          String command = String.format("java -jar performance.jar %s %s %s", genAll, numViews, numberOfViews);
          runCommandSync(command);

////           run this code if we want to only use one JVM process
//           JavaPerformanceProcess.main(new String[] { String.valueOf(genAll), String.valueOf(numViews), String.valueOf(numberOfViews) });

        }

      }

      System.out.println("COMPLETED!");

    } catch (Exception e) {
      e.printStackTrace();
    }
    PerformanceRecorder.stopRecording();

    long stopTime = System.currentTimeMillis();
    System.out.println("DURATION: " + (stopTime - startTime) + " ms");
    Thread.sleep(5000);
    System.exit(0);
  }

  public static void runCommandSync(String command) throws IOException, InterruptedException {
    runCommandSync(command.split(" "));
  }

  public static void runCommandSync(String... command) throws IOException, InterruptedException {
    System.out.println("Execute Sync: " + String.join(" ", command));
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.inheritIO();
//    processBuilder.redirectOutput(Redirect.appendTo(logFile));
//    processBuilder.redirectError(Redirect.appendTo(logFile));
    Process process = processBuilder.start();
    int exitCode = process.waitFor();
    System.out.println("Exited with error code : " + exitCode);
  }
}
