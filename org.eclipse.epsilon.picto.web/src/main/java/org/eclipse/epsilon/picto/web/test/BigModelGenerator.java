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

/**
 * 
 */
package org.eclipse.epsilon.picto.web.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.NamedElement;
import org.eclipse.gmt.modisco.java.SingleVariableDeclaration;
import org.eclipse.gmt.modisco.java.Type;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.java.emf.meta.JavaFactory;
import org.eclipse.gmt.modisco.java.emf.meta.JavaPackage;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;

public class BigModelGenerator {

  static final int NUMBER_OF_ITERATION = 20;

  /**
   * @param args
   */
  public static void main(String[] args) {

    try {

      JavaPackage.eINSTANCE.eClass();

      File modelFile = new File(PictoApplication.WORKSPACE + "/java/eol.xmi");
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
      Resource resourceOriginal = resourceSet
          .getResource(org.eclipse.emf.common.util.URI.createFileURI(modelFile.getAbsolutePath()), true);
      
      File bigModelFile = new File(PictoApplication.WORKSPACE + "/java/java.big.xmi");
      if (!bigModelFile.exists())
        bigModelFile.createNewFile();
      XMIResource bigResource = new XMIResourceImpl();
      bigResource.setURI(org.eclipse.emf.common.util.URI.createFileURI(bigModelFile.getAbsolutePath()));
      resourceSet.getResources().add(bigResource);


      String sourcePath = "D:\\PROJECTS\\epsilon\\plugins\\";

      System.out.println("Get all projects' paths ...");
      List<File> projectFiles = getAllProjectFiles(sourcePath);

      System.out.println("Found " + projectFiles.size() + " project files");
      int i = 0;
      for (File projectFile : projectFiles) {
        i++;
        System.out.println(
            i + " of " + projectFiles.size() + ": Processing project " + projectFile.getAbsolutePath() + "...");

        // initialise project description, create one if none existed
        String projectFileName = projectFile.getAbsolutePath();
        Path path = new Path(projectFileName);
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
          System.out.println("This is a Java project ...");

          DiscoverJavaModelFromJavaProject javaDiscoverer = new DiscoverJavaModelFromJavaProject();
          javaDiscoverer.setDeepAnalysis(false);
          try {
            javaDiscoverer.discoverElement(javaProject, new NullProgressMonitor());
            Resource javaResource = javaDiscoverer.getTargetModel();
            bigResource.getContents().addAll(javaResource.getContents());
            System.out.println("SUCCESS!!!");
            javaResource.getContents().clear();
            javaResource.unload();
          } catch (Exception exe) {
            System.out.println("FAIL!!!");
            exe.printStackTrace();
          }
        } catch (JavaModelException e1) {
          // e1.printStackTrace();
        }

        // close and delete the project
        try {
          System.out.println("Closing the project ...");
          project.close(null);
          project.delete(false, null);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

     
      // loop n times and copy accumulate the models in one big resource
      for (int it = 1; it <= NUMBER_OF_ITERATION; it++) {
        System.out.println("Iteration " + it);
        resourceOriginal.load(null);

        // rename namedElements by adding postfix which the iterator number
        TreeIterator<EObject> iterator = resourceOriginal.getAllContents();
        while (iterator.hasNext()) {
          EObject eObject = iterator.next();
          if (eObject instanceof Model) {
            String name = ((Model) eObject).getName();
            ((Model) eObject).setName(name + it);
          } else if (eObject instanceof NamedElement) {
            String name = ((NamedElement) eObject).getName();
            ((NamedElement) eObject).setName(name + it);
          }
        }
        bigResource.getContents().addAll(EcoreUtil.copyAll(resourceOriginal.getContents()));

        resourceOriginal.unload();
      }

      Map<Object, Object> options = new HashMap<>();
      options.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
      options.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

      int eObjectCounter = 0;
      List<ClassDeclaration> classList = new ArrayList<>();
      List<FieldDeclaration> fieldList = new ArrayList<>();
      List<MethodDeclaration> methodList = new ArrayList<>();

      Set<String> types = new HashSet<>();

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

        eObjectCounter += 1;
        bigResource.setID(eObject, "e" + eObjectCounter);
      }

      System.out.println("Number of objects: " + eObjectCounter);
      System.out.println("Number of classes: " + classList.size());
      System.out.println("Number of fields: " + fieldList.size());
      System.out.println("Number of methods: " + methodList.size());

      bigResource.save(options);

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
