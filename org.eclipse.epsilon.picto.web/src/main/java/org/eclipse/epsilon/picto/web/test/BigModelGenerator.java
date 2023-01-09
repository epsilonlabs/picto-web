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

public class BigModelGenerator {

  static final int NUMBER_OF_ITERATION = 1;

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

      Resource resource = resourceSet
          .getResource(org.eclipse.emf.common.util.URI.createFileURI(modelFile.getAbsolutePath()), true);

      File bigModelFile = new File(PictoApplication.WORKSPACE + "/java/java.xmi");
      if (!bigModelFile.exists())
        bigModelFile.createNewFile();
      XMIResource bigResource = new XMIResourceImpl();
      bigResource.setURI(org.eclipse.emf.common.util.URI.createFileURI(bigModelFile.getAbsolutePath()));
      resourceSet.getResources().add(bigResource);

      // loop n times and copy accumulate the models in one big resource
      for (int i = 1; i <= NUMBER_OF_ITERATION; i++) {
        System.out.println("Iteration " + i);
        resource.load(null);

        // rename namedElements by adding postfix which the iterator number
        TreeIterator<EObject> iterator = resource.getAllContents();
        while (iterator.hasNext()) {
          EObject eObject = iterator.next();
          if (eObject instanceof Model) {
            String name = ((Model) eObject).getName();
            ((Model) eObject).setName(name + i);
          } else if (eObject instanceof NamedElement) {
            String name = ((NamedElement) eObject).getName();
            ((NamedElement) eObject).setName(name + i);
          }
        }
        bigResource.getContents().addAll(EcoreUtil.copyAll(resource.getContents()));

        resource.unload();
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
          
          MethodDeclaration m = (MethodDeclaration) eObject;
          m.getReturnType();
          SingleVariableDeclaration type = m.getParameters().get(0);

        }

        eObjectCounter += 1;
        bigResource.setID(eObject, "e" + eObjectCounter);
      }

      System.out.println("Number of objects: " + eObjectCounter);
      System.out.println("Number of classes: " + classList.size());
      System.out.println("Number of fields: " + fieldList.size());
      System.out.println("Number of methods: " + methodList.size());

//      bigResource.save(options);

      
//      JavaFactory factory = JavaFactory.eINSTANCE;
//      FieldDeclaration x = factory.createFieldDeclaration();
//      VisibilityKind c = x.getModifier().getVisibility();
      
      
      // move a class, interface to another package

      // update methods', variables, parameters' names

      // remove elements,

      // delete a class, interface

      // copy another class, interface

//      List<?> list = new ArrayList<>(types);
//      list.sort(null);
//      list.forEach(e -> System.out.println(e));

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
