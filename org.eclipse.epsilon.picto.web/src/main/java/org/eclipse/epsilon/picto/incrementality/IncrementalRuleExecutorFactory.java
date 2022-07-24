package org.eclipse.epsilon.picto.incrementality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.IExecutableModuleElementParameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.erl.execute.RuleExecutorFactory;
import org.eclipse.epsilon.picto.Layer;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.dom.Patch;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRule;

public class IncrementalRuleExecutorFactory extends RuleExecutorFactory {

	private IncrementalResource incrementalResource;

	public IncrementalRuleExecutorFactory(IncrementalResource incrementalResource) {
		super();
		this.incrementalResource = incrementalResource;
	}

	@Override
	protected Object executeImpl(IExecutableModuleElementParameter moduleElement, IEolContext context, Object parameter)
			throws EolRuntimeException {
		// TODO Auto-generated method stub
		return super.executeImpl(moduleElement, context, parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void postExecuteSuccess(ModuleElement moduleElement, Object result, IEolContext context) {
		super.postExecuteSuccess(moduleElement, result, context);

		if (moduleElement instanceof IncrementalLazyGenerationRule) {
			IncrementalLazyGenerationRule rule = (IncrementalLazyGenerationRule) moduleElement;
			System.out.println("=== Rule: " + rule.getName() + " ===");
			if (result instanceof ArrayList) {
//				for (Object element : (ArrayList<?>) result) {
//					updateIncrementalResource(element, rule);
//				}
			} else {
				updateIncrementalResource(result, rule);
			}
			System.out.println();
			System.console();
		}
	}

	private void updateIncrementalResource(Object result, IncrementalLazyGenerationRule rule) {
		LazyGenerationRuleContentPromise content = (LazyGenerationRuleContentPromise) result;
		Variable pathVar = content.getVariables().stream().filter(v -> v.getName().equals("path")).findFirst()
				.orElse(null);
		Object varValue = pathVar.getValue();
		Collection<String> path = Arrays.asList("");
		if (varValue instanceof Collection<?> && !((Collection<?>) varValue).isEmpty()) {
			path = ((Collection<?>) varValue).stream().map(Objects::toString).collect(Collectors.toList());
			String pathString = "/" + String.join("/", path);
			System.out.println(pathString);
			generateView(content);
			if (pathString != null) {
				IEolModule module = (IEolModule) rule.getModule();
				this.updateIncrementalResource(module, pathString);
//				this.incrementalResource.printIncrementalRecords();
			}
		}

		System.console();
	}

	private void updateIncrementalResource(IEolModule module, String pathString) {
		for (IPropertyAccess propertyAccess : ((IncrementalLazyEgxModule) module).getPropertyAccessRecorder()
				.getPropertyAccesses().all()) {
			GenerationRulePropertyAccess generationRulePropertyAccess = (GenerationRulePropertyAccess) propertyAccess;

			EObject contextElement = (EObject) generationRulePropertyAccess.getContextElement();
			Resource contextResource = null;
			String contextElementId = null;
			if (contextElement != null) {
				contextResource = contextElement.eResource();
				contextElementId = contextResource.getURIFragment(contextElement);
			}

			EObject modelElement = (EObject) generationRulePropertyAccess.getModelElement();
			Resource elementResource = modelElement.eResource();
			String modelElementId = elementResource.getURIFragment(modelElement);

			EStructuralFeature property = modelElement.eClass()
					.getEStructuralFeature(generationRulePropertyAccess.getPropertyName());
			Object value = modelElement.eGet(property);

			PropertyAccessRecord record = new PropertyAccessRecord(module.getFile().getAbsolutePath(),
					generationRulePropertyAccess.getRule().getName(), contextResource.getURI().toFileString(),
					contextElementId, elementResource.getURI().toFileString(), modelElementId, property.getName(),
					value, pathString);
			System.out.println("added: " + record);
			this.incrementalResource.add(record);
		}
		((IncrementalLazyEgxModule) module).getPropertyAccessRecorder().getPropertyAccesses().clear();
		System.console();
	}

	@SuppressWarnings("unchecked")
	private void generateView(LazyGenerationRuleContentPromise instance) {

		String format = "html";
		String icon = "diagram-cccccc";
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
				if (varValue instanceof Iterable<?>) {
					for (Object layerMapObject : (Iterable<?>) varValue) {
						if (layerMapObject instanceof Map<?, ?>) {
							Map<Object, Object> layerMap = (Map<Object, Object>) layerMapObject;
							Layer layer = new Layer();
							layer.setId(layerMap.get("id") + "");
							layer.setTitle(layerMap.get("title") + "");
							if (layerMap.containsKey("active")) {
								layer.setActive((boolean) layerMap.get("active"));
							}
							layers.add(layer);
						} else {
							layers.add((Layer) layerMapObject);
						}
					}
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

	}
}
