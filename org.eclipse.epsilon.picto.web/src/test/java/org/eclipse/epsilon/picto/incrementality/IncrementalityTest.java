package org.eclipse.epsilon.picto.incrementality;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.WebEglPictoSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

class IncrementalityTest {

	private static File pictoFile;
	private static File modelFile;
	private static File modelFileBackup;
	private WebEglPictoSource eglPictoSource;
	private XMIResource res;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		PictoPackage.eINSTANCE.eClass();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {

	}

	void setUp(String pictoFileName, String modelFileName) throws Exception {
		pictoFile = new File(PictoApplication.WORKSPACE + pictoFileName);
		modelFile = new File(PictoApplication.WORKSPACE + modelFileName);
		modelFileBackup = new File(modelFile.getAbsolutePath() + ".backup");

		// backup model file
		Files.copy(modelFile, modelFileBackup);

		eglPictoSource = new WebEglPictoSource();
		eglPictoSource.transform(pictoFile);

		res = (new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath())));
		res.load(null);
	}

	@AfterEach
	void tearDown() throws Exception {
		res.unload();
		modelFile.delete();
		Files.copy(modelFileBackup, modelFile);
		modelFileBackup.delete();
	}

	@Test
	void testUpdateEglDoc() throws Exception {
		try {
			setUp("egldoc-standalone.picto", "egldoc/Families.ecore");

			EObject eObject = res.getEObject("//Bike");
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			eObject.eSet(eNameFeature, "Bicycle");
			res.save(null);
			
			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
					"/families/Bicycle"
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testUpdateProperty() throws Exception {
		try {
			setUp("socialnetwork.model.picto", "socialnetwork.model");
			
			EObject eObject = res.getEObject("3"); // get Charlie (id = 3)
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			// update the name to 'Dan'
			eObject.eSet(eNameFeature, "Dan");
			res.save(null);

			
			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
					"/Social Network" //
					, "/Social Network/Dan" //
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	@Test
	void testDeleteElement() throws Exception {
		try {
			setUp("socialnetwork.model.picto", "socialnetwork.model");

			EObject eObject = res.getEObject("2"); // get Bob (id = 2)
			EcoreUtil.delete(eObject);
			res.save(null);

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
					"/Social Network" //
					, "/Social Network/Alice" //
					, "/Stats" //
					, "/Custom/Alice and Bob" //
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testRemoveReference() throws Exception {
		try {
			setUp("socialnetwork.model.picto", "socialnetwork.model");

			EObject alice = res.getEObject("1"); // get Alice (id = 1)
			EClass person = alice.eClass();
			EStructuralFeature likesProperty = person.getEStructuralFeature("likes");

			EObject bob = res.getEObject("2"); // get Alice (id = 2)
			EList<EObject> likes = (EList<EObject>) alice.eGet(likesProperty);
			likes.remove(bob);

			res.save(null);

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
					"/Social Network" //
					, "/Social Network/Alice" //
					, "/Stats" //
					, "/Custom/Alice and Bob" //
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddReference() throws Exception {
		try {

			setUp("socialnetwork.model.picto", "socialnetwork.model");

			EObject sn = res.getEObject("0"); // get Alice (id = 1)
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

			EList<EObject> dislikes = (EList<EObject>) alice.eGet(dislikesProperty);
			dislikes.add(dan);

			res.setID(dan, "4");
			res.save(null);

			eglPictoSource = new WebEglPictoSource();
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
					"/Social Network" //
					, "/Social Network/Alice" //
					, "/Social Network/Dan" //
					, "/Stats" //
					, "/Custom/Alice and Bob" //
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddElement() throws Exception {
		try {
			setUp("socialnetwork.model.picto", "socialnetwork.model");

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
			Map<String, String> generatedViews = eglPictoSource.transform(pictoFile);

			Arrays.asList( //
//					"/Social Network" //
//					, 
					"/Social Network/Dan" //
//					, "/Stats" //
			).stream().forEach(path -> {
				assertTrue(generatedViews.keySet().contains(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
