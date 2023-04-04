/*********************************************************************
* Copyright (c) 2023 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* @author Alfa Yohannis
**********************************************************************/

package org.eclipse.epsilon.picto.web.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.modisco.java.ClassDeclaration;
import org.eclipse.modisco.java.FieldDeclaration;
import org.eclipse.modisco.java.MethodDeclaration;
import org.eclipse.modisco.java.Model;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.modisco.java.emf.JavaPackage;

public class BigModelGenerator {

  static final int NUMBER_OF_ITERATION = 20;

  public void generate() {

    List<Record> records = new ArrayList<>();

    try {

      JavaPackage.eINSTANCE.eClass();

//      File modelFile = new File(PictoApplication.WORKSPACE + "/java/eol.xmi");
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

      File bigModelFile = new File("java.big.xmi");
      if (!bigModelFile.exists())
        bigModelFile.createNewFile();
      XMIResource bigResource = new XMIResourceImpl();
      bigResource.setURI(org.eclipse.emf.common.util.URI.createFileURI(bigModelFile.getAbsolutePath()));
      resourceSet.getResources().add(bigResource);

      String sourcePath = "D:\\PROJECTS\\epsilon\\";

      System.out.println("Get all projects under " + sourcePath);
      System.out.println();
      List<File> projectFiles = getAllProjectFiles(sourcePath);

      System.out.println("Found " + projectFiles.size() + " project files");
      System.out.println();

      int i = 0;
      for (File projectFile : projectFiles) {
        i++;
        System.out.println(
            i + " of " + projectFiles.size() + ": Processing project " + projectFile.getAbsolutePath() + "...");

        // initialise project description, create one if none existed
        String projectFileName = projectFile.getAbsolutePath();
        Path path = new Path(projectFileName);
//        Path path = new Path("D:\\PROJECTS\\epsilon\\plugins\\org.eclipse.epsilon.eol.engine\\.project");
        IProjectDescription description;
        if (path.toFile().exists()) {
          try {
            description = ResourcesPlugin.getWorkspace().loadProjectDescription(path);
            description.setName(projectFile.getParentFile().getName());
          } catch (Exception e) {
            try {
              e.printStackTrace();
              description = ResourcesPlugin.getWorkspace().newProjectDescription(projectFile.getName());
              path = new Path(projectFile.getParentFile().getAbsolutePath());
              description.setLocation(path);
              String[] natureIds = new String[] { JavaCore.NATURE_ID };
              description.setNatureIds(natureIds);
              description.setName(projectFile.getParentFile().getName());
            } catch (Exception e2) {
              e2.printStackTrace();
              System.out.println(" FAILED");
              continue;
            }
          }
        } else {
          description = ResourcesPlugin.getWorkspace().newProjectDescription(projectFile.getName());
          path = new Path(projectFile.getParentFile().getAbsolutePath());
          description.setLocation(path);
          String[] natureIds = new String[] { JavaCore.NATURE_ID };
          description.setNatureIds(natureIds);
          description.setName(projectFile.getParentFile().getName());
        }

        // create eclipse project
        IProject project;
        try {
          project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
        } catch (Exception exe) {
          continue;
        }

        if (!project.exists()) {
          try {
            project.create(description, null);
            project.open(null);
          } catch (Exception exe) {
            exe.printStackTrace();
          }
        } else {
          try {
            project.close(null);
            project.delete(false, null);
            project.create(description, null);
            project.open(null);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }

        // create java project
        IJavaProject javaProject = JavaCore.create(project);
        try {
          javaProject.open(null);
//          System.out.println("This is a Java project. Generating Java Model ... ");

          DiscoverJavaModelFromJavaProject javaDiscoverer = new DiscoverJavaModelFromJavaProject();
          javaDiscoverer.setDeepAnalysis(false);
          try {
            long start = System.currentTimeMillis();
            javaDiscoverer.discoverElement(javaProject, new NullProgressMonitor());
            Resource javaResource = javaDiscoverer.getTargetModel();
            long time = System.currentTimeMillis() - start;
            Model model = (Model) javaResource.getContents().get(0);
            String name = model.getName();
            model.setName("M" + (i - 1));
            records.add(new Record(model.getName() + "." + name, time));
            bigResource.getContents().addAll(javaResource.getContents());
            System.out.println("SUCCESS!!!");
            javaResource.getContents().clear();
            javaResource.unload();
          } catch (Exception exe) {
            System.out.println("FAIL!!!");
            exe.printStackTrace();
          }
        } catch (JavaModelException e1) {
//          e1.printStackTrace();
          System.out.println("This is not a (valid) Java project");
        }

        // close and delete the project
        try {
          System.out.print("Closing the project ... ");
          project.close(null);
          project.delete(false, null);
          System.out.println("Done");
        } catch (Exception ex) {
          ex.printStackTrace();
        }

        System.out.println();
      }

      System.out.println("Model,Time");
      records.sort(Comparator.comparingLong(Record::getTime));
      for (Record record : records) {
        System.out.println(record.getModelName() + "," + record.getTime());
      }

//      Set<String> modelNames = new HashSet<>(Arrays.asList(new String[] {"M294", "M330", "M296", "M311", "M317" }));
//      Set<String> modelNames = new HashSet<>(Arrays.asList(new String[] {"M38", "M232", "M241", "M22", "M42", "M1", "M289" }));
//
//      bigResource.getContents().removeIf(m -> m instanceof Model && !modelNames.contains(((Model) m).getName()));

//      List<EObject> temp = new ArrayList<>();
//      temp.addAll(bigResource.getContents());
//      temp.remove(temp.size()- 1);
//      bigResource.getContents().removeAll(temp);

//      List<String> subList = records.subList(0, (records.size() * 9/ 10) - 1).stream()
//          .map(r -> r.getModelName()).collect(Collectors.toList());
//      
//      for (String modelName : subList) {
//        Iterator<EObject> iter = bigResource.getContents().iterator();
//        while(iter.hasNext()) {
//          Model model = (Model) iter.next();
//          String[] names = modelName.split("\\.");
//          if (names.length > 0 && names[0].equals(model.getName())) {
//            iter.remove();
//          }
//        }
//      }

      int eObjectCounter = 0;
      List<ClassDeclaration> classList = new ArrayList<>();
      List<FieldDeclaration> fieldList = new ArrayList<>();
      List<MethodDeclaration> methodList = new ArrayList<>();

      Set<String> types = new HashSet<>();

      System.out.println("Assigning IDs to every element");
      System.out.println();
      TreeIterator<EObject> iterator = bigResource.getAllContents();
      while (iterator.hasNext()) {
        EObject eObject = iterator.next();
        String name = eObject.eClass().getName();
        types.add(name);
        if (eObject instanceof ClassDeclaration) {
          classList.add((ClassDeclaration) eObject);
        }
        if (eObject instanceof FieldDeclaration) {
          fieldList.add((FieldDeclaration) eObject);
        }
        if (eObject instanceof MethodDeclaration) {
          methodList.add((MethodDeclaration) eObject);
        }

        eObjectCounter++;
        bigResource.setID(eObject, "e" + eObjectCounter);
      }

      System.out.println("FINISHED!");
      System.out.println();

      System.out.print("Saving model ... ");
      Map<Object, Object> options = new HashMap<>();
      options.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
      options.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
      bigResource.save(options);
      System.out.println("SAVED");
      System.out.println();

      System.out.println("Number of objects: " + eObjectCounter);
      System.out.println("Number of classes: " + classList.size());
      System.out.println("Number of fields: " + fieldList.size());
      System.out.println("Number of methods: " + methodList.size());
      System.out.println();

//      List<?> list = new ArrayList<>(types);
//      list.sort(null);
//      list.forEach(e -> System.out.println(e));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void listFiles(String path, List<File> files) {
    File dir = new File(path);
    for (File file : dir.listFiles()) {
      if (file.isFile() && file.getName().contains(".project")) {
        files.add(file);
      } else if (file.isDirectory()) {
        listFiles(file.getAbsolutePath(), files);
      }
    }
  }

  private static List<File> getAllProjectFiles(String sourcePath) {
    List<File> files = new ArrayList<>();
    listFiles(sourcePath, files);
    return files;
  }
}
