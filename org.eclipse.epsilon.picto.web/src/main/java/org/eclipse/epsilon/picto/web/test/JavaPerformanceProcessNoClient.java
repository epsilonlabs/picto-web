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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.incrementality.AccessGraphResource;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PromisesGenerationListener;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.InterfaceDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
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
public class JavaPerformanceProcessNoClient {

  static final String PICTO_WEB_ADDRESS = "http://localhost:8081/pictojson/picto?";
  public static final String WEB_SOCKET_ADDRESS = "ws://localhost:8081/picto-web";
  public static final String PICTO_FILE = "/java/java.picto";
  private static final String MODEL_TMP = "/java.xmi";
  private static final String MODEL = "/java/java.xmi";
  public static final String PICTO_TOPIC = "/topic/picto";

  private static final File modelFile = new File(PictoApplication.WORKSPACE + File.separator + MODEL);
  private static final File modelFileTmp = new File(PictoApplication.TEMP + File.separator + MODEL_TMP);

  private static final XMIResource resource = new XMIResourceImpl(URI.createFileURI(modelFileTmp.getAbsolutePath()));

  static final Random random = new Random();

  private static Server server;

  static ObjectMapper mapper = new ObjectMapper();
  static List<String> generatedViews = new LinkedList<>();
  private static List<String> classNames = new LinkedList<>();
  private static List<String> classIDs = new LinkedList<>();
  private static List<String> modifiedViews = new LinkedList<>();

  public static void main(String... args) throws Exception {

    boolean genAll = Boolean.parseBoolean(args[0]);
    int numOfAffectedViews = Integer.parseInt(args[1]);

    PerformanceRecorder.genenerateAll = genAll;
    PictoApplication.setNonIncremental(PerformanceRecorder.genenerateAll);

    int numberOfIteration = 13; // Number of iteration measuring for each number of affected views
    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.

    /** comment this if we want to test using the big model */
//  numberOfClients = 1; // number of clients subscribed to Picto Web's STOMP server.
//  numberOfIteration = 3; // Number of iteration measuring for each number of affected views
//  MODEL_ORIGINAL = "/java/java.small.xmi";

    PerformanceRecorder.globalNumberOfAffectedViews = numOfAffectedViews;
    PerformanceRecorder.setOutputFile(new File("data/selective.csv"));
    PerformanceRecorder.startRecording();

    // start Picto Web server
    System.out.println("Starting server ...");
    server = new Server();
    server.start();
    Thread.sleep(1000);

    Object invalidatedViewsWaiter = new Object();

    Map<Object, Object> loadOptions = ((XMIResourceImpl) resource).getDefaultLoadOptions();
    loadOptions.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

    Map<Object, Object> saveOptions = ((XMIResourceImpl) resource).getDefaultSaveOptions();
    saveOptions.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

    JavaPackage.eINSTANCE.eClass();

    System.out.print("Loading Model ... ");
    resource.load(loadOptions);
    System.out.println("Done");

    classIDs.clear();
    classNames.clear();

    TreeIterator<EObject> treeIterator = resource.getAllContents();
    while (treeIterator.hasNext()) {
      EObject eObject = treeIterator.next();
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
        classIDs.add(id);
      }
    }

    // modify
    try {

      for (int iterationIndex = 1; iterationIndex <= numberOfIteration; iterationIndex++) {

        final int index = iterationIndex;

        PictoApplication.setPromisesGenerationListener(new PromisesGenerationListener() {
          @Override
          public void onGenerated(Set<String> invalidatedViews) {
            synchronized (invalidatedViewsWaiter) {
              generatedViews.addAll(invalidatedViews);
              Collections.sort(generatedViews);

              System.out
                  .println("MODIFIED VIEWS = " + modifiedViews.size() + ", GENERATED VIEWS = " + generatedViews.size());
              if (index > 1 && !PictoApplication.isNonIncremental()) {
                assertThat(modifiedViews).isSubsetOf(generatedViews);
              }

              // notify the main thread to continue iteration
              invalidatedViewsWaiter.notify();
            }
          }
        });

        PerformanceRecorder.globalNumberIteration = iterationIndex;

        System.out.println("\n## GEN ALL: " + PerformanceRecorder.genenerateAll + " AFFECTED VIEWS: "
            + numOfAffectedViews + ", ITERATION: " + iterationIndex + " ##");

        generatedViews.clear();

//            resource.load(options);

        modifiedViews.clear();
        modifiedViews.add("/");
        for (int i = 0; i < numOfAffectedViews; i++) {
          modifiedViews.add("/" + classNames.get(i));
          String id1 = classIDs.get(i);
          ClassDeclaration class1 = (ClassDeclaration) resource.getEObject(id1);
          List<BodyDeclaration> elementList1 = class1.getBodyDeclarations().stream()
              .filter(b -> b instanceof FieldDeclaration /* || b instanceof MethodDeclaration */)
              .collect(Collectors.toList());

          /** update the names of fields **/
          if (elementList1.size() > 0) {
            BodyDeclaration element = elementList1.get(0);
            if (element instanceof FieldDeclaration) {
              VariableDeclarationFragment variable = ((FieldDeclaration) element).getFragments().get(0);
//              System.out.print(classNames.get(i) + ": CHANGE " + variable.getName() + " TO ");
              String[] segments = variable.getName().split("_");
              if (segments.length > 1) {

                variable.setName(segments[0] + "_" + (Integer.parseInt(segments[1]) + 1));
              } else {
                variable.setName(segments[0] + "_" + "1");
              }
//              System.out.println(variable.getName());

            } else if (element instanceof MethodDeclaration) {
              MethodDeclaration method = (MethodDeclaration) element;
              String[] segments = method.getName().split("_");
              if (segments.length > 1) {
                method.setName(segments[0] + "_" + (Integer.parseInt(segments[1]) + 1));
              } else {
                method.setName(segments[0] + "_" + "1");
              }
            }

          }
        }

        resource.save(saveOptions);

        // copy model to the watched directory
        boolean isCopySuccess = false;
        while (!isCopySuccess) {
          try {
            Files.copy(modelFileTmp.toPath(), (new File(modelFileTmp.getAbsolutePath() + ".backup")).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
            Files.move(modelFileTmp.toPath(), modelFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.move((new File(modelFileTmp.getAbsolutePath() + ".backup")).toPath(), modelFileTmp.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

            isCopySuccess = true;
          } catch (Exception e) {
            Thread.sleep(100);
            isCopySuccess = false;
          }
        }

        PerformanceRecorder.startTime = System.currentTimeMillis();

        // wait until we get the paths of invalidated views
        synchronized (invalidatedViewsWaiter) {
          invalidatedViewsWaiter.wait();
          System.out.println("Expected Views : " + generatedViews);
        }

        JavaPerformanceProcessNoClient.waitingBackgroundTasks();

        Thread.sleep(1000);

      }

      PerformanceRecorder.stopRecording();
      server.stop();

      Thread.sleep(1000);
      System.out.println("FINISHED!");

    } catch (Exception e) {
      e.printStackTrace();
    }

    File lockFile = new File(PictoApplication.TEMP + File.separator + "performance.lock");
    if (lockFile.exists()) {
      if (lockFile.delete()) {
      }
    }
    System.exit(0);
  }

  /***
   * 
   * @throws InterruptedException
   */
  public static void waitingBackgroundTasks() throws InterruptedException {
    long start = System.currentTimeMillis();
    System.out.print("Waiting AccessGraphResource task executor to complete ");
    while (AccessGraphResource.getExecutorService().getQueue().size() > 0) {
      System.out.print(AccessGraphResource.getExecutorService().getQueue().size());
      System.out.print(".");
      Thread.sleep(1000);
    }
    System.out.println(" Done: " + (System.currentTimeMillis() - start) + " ms");
  }
}
