package org.eclipse.epsilon.picto.incrementality;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.FileWatcher;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.WebEglPictoSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncrementalityTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	void testOptimisation() throws Exception {
		try {
			PictoPackage.eINSTANCE.eClass();

			FileWatcher fileWatcher = new FileWatcher();
			fileWatcher.start();
			File pictoFile = new File(PictoApplication.WORKSPACE + "socialnetwork.model.picto");
			File metamodelFile = new File(PictoApplication.WORKSPACE + "socialnetwork.ecore");
			File modelFile = new File(PictoApplication.WORKSPACE + "socialnetwork.model");
			File egxFile = new File(PictoApplication.WORKSPACE + "picto" + File.separator + "socialnetwork.egx");

			WebEglPictoSource eglPictoSource = new WebEglPictoSource();
			eglPictoSource.transform(pictoFile);
			
			XMIResource res = (new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath())));
			res.load(null);

			IModel model = new InMemoryEmfModel("M", res);
			((InMemoryEmfModel) model).setExpand(true);
			
			EObject eObject = res.getEObject("3");
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			String name = (String) eObject.eGet(eNameFeature);
			eObject.eSet(eNameFeature, name + "e");
			model.store();
			
			System.out.println("Retransform");
			eglPictoSource.transform(pictoFile);
			
			System.console();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
