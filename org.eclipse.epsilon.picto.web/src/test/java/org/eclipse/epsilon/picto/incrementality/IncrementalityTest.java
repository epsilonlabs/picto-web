package org.eclipse.epsilon.picto.incrementality;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.spec.EglTemplateSpecification;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.Layer;
import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.ViewContent;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.dom.Patch;
import org.eclipse.epsilon.picto.transformers.ViewContentTransformer;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoResponse;
import org.eclipse.epsilon.picto.web.ViewContentCache;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			File metamodelFile = new File(PictoApplication.WORKSPACE + "socialnetwork.ecore");
			File modelFile = new File(PictoApplication.WORKSPACE + "socialnetwork.model");
			File egxFile = new File(PictoApplication.WORKSPACE + "picto" + File.separator + "socialnetwork.egx");

			// register metamodel
			org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
					.createFileURI(metamodelFile.getAbsolutePath());
			EmfUtil.register(uri, EPackage.Registry.INSTANCE);

			// load model
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("model",
					new XMIResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",
					new XMIResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
			Resource resource = resourceSet
					.getResource(org.eclipse.emf.common.util.URI.createFileURI(modelFile.getAbsolutePath()), true);
			resource.load(null);

			IModel model = new InMemoryEmfModel("M", resource);
			((InMemoryEmfModel) model).setExpand(true);

			// load transformation
			IncrementalResource incrementalResource = new PropertyAccessRecordTable();
			IncrementalLazyEgxModule module = new IncrementalLazyEgxModule(incrementalResource);
			IEolContext context = module.getContext();
			URI transformationUri = egxFile.toURI();

			module.parse(transformationUri);

			// add model to context
			context.getModelRepository().addModel(model);

			/** execute transformation after model is changed **/
			List<LazyGenerationRuleContentPromise> instances = (List<LazyGenerationRuleContentPromise>) module.execute();
			((IncrementalLazyEgxModule) module).startRecording();
			generateViews(instances, module);
			((IncrementalLazyEgxModule) module).stopRecording();

			((IncrementalLazyEgxModule) module).getPropertyAccessRecorder().getPropertyAccesses().clear();
			
			incrementalResource.printIncrementalRecords();

			// modify the model
			EObject eObject = (EObject) model.getElementById("alice");
			EStructuralFeature eNameFeature = eObject.eClass().getEStructuralFeature("name");
			System.out.println("Element's name: " + (String) eObject.eGet(eNameFeature));
			eObject.eSet(eNameFeature, "Amber");

			/** reexecute transformation after model is changed **/
			instances = (List<LazyGenerationRuleContentPromise>) module.execute();
			((IncrementalLazyEgxModule) module).startRecording();
			generateViews(instances, module);
			((IncrementalLazyEgxModule) module).stopRecording();

			incrementalResource.printIncrementalRecords();

			System.out.println("Targets of Modified Properties:" + incrementalResource.getModifiedPropertiesTargets().size());
			
			for (PropertyAccessRecord property : incrementalResource.getModifiedPropertiesTargets()) {
				System.out.println(property);
			}
			assertEquals(incrementalResource.getModifiedPropertiesTargets().size(), 2);
			System.out.println("Finished!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateViews(List<LazyGenerationRuleContentPromise> instances, IncrementalLazyEgxModule module) {
		for (LazyGenerationRuleContentPromise instance : instances) {
			
			String format = "svg";
			String icon = "";
			List<Patch> patches = new ArrayList<>(1);
			Collection<String> path = Arrays.asList("");
			String pathString = null;
			List<Layer> layers = new ArrayList<>();
			Variable layersVariable = null;
			Integer position = null;

			Collection<Variable> instanceVariables = instance.getVariables();

			for (Variable variable : instanceVariables) {
				Object varValue = variable.getValue();
				switch (variable.getName()) {
				case "format": {
					format = varValue + "";
					break;
				}
				case "path": {
					if (!(varValue instanceof Collection)) {
						(path = (Collection<String>) (varValue = new ArrayList<>(1))).add(Objects.toString(varValue));
					} else if (!((Collection<?>) varValue).isEmpty()) {
						path = ((Collection<?>) varValue).stream().map(Objects::toString).collect(Collectors.toList());
					}
					pathString = "/" + String.join("/", path);
					break;
				}
				case "icon": {
					icon = varValue + "";
					break;
				}
				case "position": {
					if (varValue instanceof Integer) {
						position = (Integer) varValue;
					} else if (varValue != null) {
						position = Integer.parseInt(varValue.toString());
					}
					break;
				}
				case "layers": {
					layersVariable = variable;
					for (Object layerMapObject : (Iterable<?>) varValue) {
						Map<Object, Object> layerMap = (Map<Object, Object>) layerMapObject;
						Layer layer = new Layer();
						layer.setId(layerMap.get("id") + "");
						layer.setTitle(layerMap.get("title") + "");
						if (layerMap.containsKey("active")) {
							layer.setActive((boolean) layerMap.get("active"));
						}
						layers.add(layer);
					}
					break;
				}
				case "patches": {
					if (varValue instanceof List) {
						patches = (List<Patch>) varValue;
					} else if (varValue instanceof Patch) {
						patches.add((Patch) varValue);
					} else if (varValue instanceof Collection) {
						patches.addAll((Collection<? extends Patch>) varValue);
					}
					break;
				}
				}

			}

			// If this is a custom view there may be an activeLayers variable in the
			// variables list
			Variable activeLayersVariable = instanceVariables.stream().filter(v -> v.getName().equals("activeLayers"))
					.findAny().orElse(null);
			if (activeLayersVariable != null) {
				Collection<?> activeLayers = (Collection<?>) activeLayersVariable.getValue();
				for (Layer layer : layers) {
					layer.setActive(activeLayers.contains(layer.getId()));
				}
			}

			// Replace layers variable from list of maps to list of Layer objects
			if (layersVariable != null) {
				instanceVariables.remove(layersVariable);
			}
			instanceVariables.add(Variable.createReadOnlyVariable("layers", layers));

			ViewTree vt = new ViewTree(instance, format, icon, position, patches, layers);
			vt.getContent();
			updateIncrementalResource(module, pathString);

		}
	}
	
	private void updateIncrementalResource(IncrementalLazyEgxModule module, String pathString) {
		for (IPropertyAccess propertyAccess : ((IncrementalLazyEgxModule) module).getPropertyAccessRecorder()
				.getPropertyAccesses().all()) {
			GenerationRulePropertyAccess generationRulePropertyAccess = (GenerationRulePropertyAccess) propertyAccess;

			EObject contextElement = (EObject) generationRulePropertyAccess.getContextElement();
			String contextResourceUri = null;
			String contextElementId = null;
			if (contextElement != null) {
				contextResourceUri = ((XMIResource) contextElement.eResource()).getURI().toString();
				contextElementId = ((XMIResource) contextElement.eResource()).getID(contextElement);
			} else {
				System.console();
			}

			EObject modelElement = (EObject) generationRulePropertyAccess.getModelElement();
			String elementResourceUri = ((XMIResource) modelElement.eResource()).getURI().toString();
			String elementId = ((XMIResource) modelElement.eResource()).getID(modelElement);

			String propertyName = generationRulePropertyAccess.getPropertyName();
			Object value = modelElement.eGet(modelElement.eClass().getEStructuralFeature(propertyName));

			PropertyAccessRecord record = new PropertyAccessRecord(module.getFile().getAbsolutePath(),
					generationRulePropertyAccess.getRule().getName(), contextResourceUri, contextElementId,
					elementResourceUri, elementId, propertyName, value, pathString);
			System.out.println("added: " + record);
			module.getIncrementalResource().add(record);
		}
		((IncrementalLazyEgxModule) module).getPropertyAccessRecorder().getPropertyAccesses().clear();
		System.console();
	}
}
