/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.incrementality;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.FileViewContentCache;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoProject;
import org.eclipse.epsilon.picto.web.WebEglPictoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

/**
 * The class is to test if Picto Web could detect accurately which views should
 * be invalidated due certain changes in a model.
 * 
 * @author Alfa Yohannis
 *
 */
class IncrementalityTest {

  private static File pictoFile;
  private static File modelFile;
  private static File modelFileBackup;
  private String modifiedFilePath;
  private WebEglPictoSource eglPictoSource;
  private XMIResource res;

  private AccessRecordResource accessRecordResource = null;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    PictoPackage.eINSTANCE.eClass();
  }

  public Set<String> setUp(String pictoFileName, String modelFileName) throws Exception {
    pictoFile = new File(PictoApplication.WORKSPACE + pictoFileName);
    modelFile = new File(PictoApplication.WORKSPACE + modelFileName);
    modelFileBackup = new File(modelFile.getAbsolutePath() + ".backup");
    modifiedFilePath = pictoFile.getAbsolutePath()
        .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath() + File.separator, "").replace("\\", "/");

    accessRecordResource = FileViewContentCache.createAccessRecordResource(pictoFileName);

    // backup model file
    Files.copy(modelFile, modelFileBackup);

    eglPictoSource = new WebEglPictoSource();
    Set<String> result = eglPictoSource.generatePromises(modifiedFilePath, PictoProject.createPictoProject(pictoFile),
        true);

    res = (new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath())));
    res.load(null);
    return result;
  }

  @AfterEach
  void tearDown() throws Exception {
    accessRecordResource.clear();
    FileViewContentCache.clear();
    res.unload();
    modelFile.delete();
    Files.copy(modelFileBackup, modelFile);
    modelFileBackup.delete();
    Thread.sleep(1000);
  }

  @Test
  void testGeneration() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());

    Set<String> generatedViews = setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    assertThat(generatedViews).containsExactlyInAnyOrder("/Social Network", "/Social Network/Alice",
        "/Social Network/Bob", "/Social Network/Charlie", "/Stats", "/Custom/Alice and Bob", "/Readme", "/");
  }

  @Test
  void testUpdateEglDoc() throws Exception {
    try {
      System.out.println("\n" + new Object() {
      }.getClass().getEnclosingMethod().getName());
      setUp("egldoc/egldoc-standalone.picto", "egldoc/egldoc/Families.ecore");

      EObject eObject = res.getEObject("//Bike");
      EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
      eObject.eSet(eNameFeature, "Bicycle");
      res.save(null);
      Thread.sleep(1000);

      eglPictoSource = new WebEglPictoSource();
    } finally {
      Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
          PictoProject.createPictoProject(pictoFile), true);
      Arrays.asList("/families/Bicycle").stream().forEach(path -> {
        assertTrue(generatedViews.contains(path));
      });
    }
  }

  @Test
  void testUpdateProperty() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject eObject = res.getEObject("3"); // get Charlie (id = 3)
    EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
    eObject.eSet(eNameFeature, "Dan");
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);
    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network",
        "/Social Network/Dan");
  }

  @Test
  void testDeleteElement() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject eObject = res.getEObject("2"); // get Bob (id = 2)
    EcoreUtil.delete(eObject);
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Alice", "/Stats");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testRemoveReference() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature likesProperty = person.getEStructuralFeature("likes");

    EObject bob = res.getEObject("2"); // get Alice (id = 2)
    EList<EObject> likes = (EList<EObject>) alice.eGet(likesProperty);
    likes.remove(bob);
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Alice", "/Stats");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testAddReference() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject sn = res.getEObject("0"); // get social Network (id = 0)
    EClass socialNetwork = sn.eClass();
    EStructuralFeature peopleProperty = socialNetwork.getEStructuralFeature("people");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature nameProperty = person.getEStructuralFeature("name");
    EStructuralFeature dislikesProperty = person.getEStructuralFeature("dislikes");

    EObject dan = EcoreUtil.create(person);
    dan.eSet(nameProperty, "Dan");

    EList<EObject> people = (EList<EObject>) sn.eGet(peopleProperty);
    people.add(dan);
    res.setID(dan, "4");

    EList<EObject> dislikes = (EList<EObject>) alice.eGet(dislikesProperty);
    dislikes.add(dan);
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);
    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Alice",
        "/Social Network/Dan", "/Stats");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testAddElement() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject sn = res.getEObject("0"); // get Alice (id = 1)
    EClass socialNetwork = sn.eClass();
    EStructuralFeature peopleProperty = socialNetwork.getEStructuralFeature("people");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature nameProperty = person.getEStructuralFeature("name");

    EObject dan = EcoreUtil.create(person);
    dan.eSet(nameProperty, "Dan");

    EList<EObject> people = (EList<EObject>) sn.eGet(peopleProperty);
    people.add(dan);
    res.setID(dan, "4");
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);
    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Dan", "/Stats");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testAddNonDeterminingElement() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature itemsProperty = person.getEStructuralFeature("items");

    EClass item = (EClass) person.getEPackage().getEClassifier("Item");
    EStructuralFeature nameProperty = item.getEStructuralFeature("name");

    EObject book = EcoreUtil.create(item);
    book.eSet(nameProperty, "Book");

    EList<EObject> items = (EList<EObject>) alice.eGet(itemsProperty);
    items.add(book);
    res.setID(book, "1_1");
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);
    assertThat(generatedViews).containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Alice");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testMultipleUpdates() throws Exception {
    System.out.println("\n" + new Object() {
    }.getClass().getEnclosingMethod().getName());
    setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

    /** first update **/
    System.out.println("### Rename Charlie to Bob ###");
    EObject eObject = res.getEObject("3"); // get Charlie (id = 3)
    EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
    // update the name to 'Bobby'
    eObject.eSet(eNameFeature, "Bobby");
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews).as("Generated views from updating name to 'Bobby'")
        .containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", "/Social Network", "/Social Network/Bobby");

    /** Second update **/
    System.out.println("### Delete the Real Bob ###");
    eObject = res.getEObject("2"); // get Bob (id = 2)
    EcoreUtil.delete(eObject);
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews2 = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews2).as("Generated views from deleting Bob").containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob",
        "/Social Network", "/Social Network/Alice", "/Stats");

    /** Third update **/
    System.out.println("### Add Dan to the people that Alice dislikes ###");
    EObject sn = res.getEObject("0"); // get social Network (id = 0)
    EClass socialNetwork = sn.eClass();
    EStructuralFeature peopleProperty = socialNetwork.getEStructuralFeature("people");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature nameProperty = person.getEStructuralFeature("name");
    EStructuralFeature dislikesProperty = person.getEStructuralFeature("dislikes");

    EObject dan = EcoreUtil.create(person);
    dan.eSet(nameProperty, "Dan");

    EList<EObject> people = (EList<EObject>) sn.eGet(peopleProperty);
    people.add(dan);
    res.setID(dan, "4");

    EList<EObject> dislikes = (EList<EObject>) alice.eGet(dislikesProperty);
    dislikes.add(dan);

    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews3 = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews3).as("Generated views from creating Dan").containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", 
        "/Social Network", "/Social Network/Alice", "/Social Network/Dan", "/Stats");

    /** 4th update **/
    System.out.println("### Add Erin ###");
    sn = res.getEObject("0"); // get Alice (id = 1)
    socialNetwork = sn.eClass();
    peopleProperty = socialNetwork.getEStructuralFeature("people");

    alice = res.getEObject("1"); // get Alice (id = 1)
    person = alice.eClass();
    nameProperty = person.getEStructuralFeature("name");

    EObject erin = EcoreUtil.create(person);
    erin.eSet(nameProperty, "Erin");

    people = (EList<EObject>) sn.eGet(peopleProperty);
    people.add(erin);

    res.setID(erin, "5");
    res.save(null);
    Thread.sleep(1000);

    eglPictoSource = new WebEglPictoSource();
    Set<String> generatedViews4 = eglPictoSource.generatePromises(modifiedFilePath,
        PictoProject.createPictoProject(pictoFile), true);

    assertThat(generatedViews4).as("Generated views from creating Erin").containsExactlyInAnyOrder("/", "/Readme", "/Custom/Alice and Bob", 
        "/Social Network", "/Social Network/Erin", "/Stats");
  }
}
