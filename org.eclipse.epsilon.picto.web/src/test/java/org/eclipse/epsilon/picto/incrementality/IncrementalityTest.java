package org.eclipse.epsilon.picto.incrementality;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

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
import org.eclipse.epsilon.picto.web.WebEglPictoSource;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

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

	public Map<String, String> setUp(String pictoFileName, String modelFileName) throws Exception {
		pictoFile = new File(PictoApplication.WORKSPACE + pictoFileName);
		modelFile = new File(PictoApplication.WORKSPACE + modelFileName);
		modelFileBackup = new File(modelFile.getAbsolutePath() + ".backup");
		modifiedFilePath = pictoFile.getAbsolutePath()
				.replace(new File(PictoApplication.WORKSPACE).getAbsolutePath() + File.separator, "").replace("\\", "/");

		accessRecordResource = FileViewContentCache.createAccessRecordResource(pictoFileName);

		// backup model file
		Files.copy(modelFile, modelFileBackup);

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> result = eglPictoSource.transform(modifiedFilePath);

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
	}

	@Disabled
	@Test
	void testGeneration() throws Exception {
		Map<String, String> generatedViews = setUp(
			"socialnetwork/socialnetwork.model.picto",
			"socialnetwork/socialnetwork.model");

		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Alice",
			"/Social Network/Bob",
			"/Social Network/Charlie",
			"/Stats",
			"/Custom/Alice and Bob",
			"/Readme");
	}

	@Disabled
	@Test
	void testUpdateEglDoc() throws Exception {
		try {
			setUp("egldoc/egldoc-standalone.picto", "egldoc/egldoc/Families.ecore");

			EObject eObject = res.getEObject("//Bike");
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			eObject.eSet(eNameFeature, "Bicycle");
			res.save(null);

			eglPictoSource = new WebEglPictoSource();
		} finally {
			Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
			Arrays.asList( //
					"/families/Bicycle").stream().forEach(path -> {
						assertTrue(generatedViews.keySet().contains(path));
					});
		}
	}

	@Disabled
	@Test
	void testUpdateProperty() throws Exception {
		setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

		EObject eObject = res.getEObject("3"); // get Charlie (id = 3)
		EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
		eObject.eSet(eNameFeature, "Dan");
		res.save(null);

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Dan");
	}

	@Test
	void testDeleteElement() throws Exception {
		setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

		EObject eObject = res.getEObject("2"); // get Bob (id = 2)
		EcoreUtil.delete(eObject);
		res.save(null);

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);

		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Alice",
			"/Stats",
			"/Custom/Alice and Bob");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testRemoveReference() throws Exception {
		setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

		EObject alice = res.getEObject("1"); // get Alice (id = 1)
		EClass person = alice.eClass();
		EStructuralFeature likesProperty = person.getEStructuralFeature("likes");

		EObject bob = res.getEObject("2"); // get Alice (id = 2)
		EList<EObject> likes = (EList<EObject>) alice.eGet(likesProperty);
		likes.remove(bob);
		res.save(null);

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Alice",
			"/Stats",
			"/Custom/Alice and Bob");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddReference() throws Exception {
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

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Alice",
			"/Social Network/Dan",
			"/Stats",
			"/Custom/Alice and Bob");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddElement() throws Exception {
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

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Dan",
			"/Stats");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddNonDeterminingElement() throws Exception {
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

		eglPictoSource = new WebEglPictoSource();
		Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);
		assertThat(generatedViews.keySet()).contains(
			"/Social Network",
			"/Social Network/Alice",
			"/Custom/Alice and Bob");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testMultipleUpdates() throws Exception {
			setUp("socialnetwork/socialnetwork.model.picto", "socialnetwork/socialnetwork.model");

			/** first update **/
			EObject eObject = res.getEObject("3"); // get Charlie (id = 3)
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			// update the name to 'Bobby'
			eObject.eSet(eNameFeature, "Bobby");
			res.save(null);

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(modifiedFilePath);

			assertThat(generatedViews.keySet())
				.as("Generated views from updating name to 'Bobby'")
				.contains("/Social Network", "/Social Network/Bobby");

			/** Second update **/
			eObject = res.getEObject("2"); // get Bob (id = 2)
			EcoreUtil.delete(eObject);
			res.save(null);

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews2 = eglPictoSource.transform(modifiedFilePath);

			assertThat(generatedViews2.keySet())
				.as("Generated views from deleting Bob")
				.contains("/Social Network", "/Social Network/Alice", "/Stats", "/Custom/Alice and Bob");

			/** Third update **/
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

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews3 = eglPictoSource.transform(modifiedFilePath);

			assertThat(generatedViews3.keySet())
				.as("Generated views from creating Dan")
				.contains("/Social Network", "/Social Network/Alice", "/Social Network/Dan", "/Stats", "/Custom/Alice and Bob");

			/** 4th update **/
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

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews4 = eglPictoSource.transform(modifiedFilePath);

			assertThat(generatedViews4.keySet())
				.as("Generated views from creating Erin")
				.contains("/Social Network", "/Social Network/Erin", "/Stats");
	}
}
