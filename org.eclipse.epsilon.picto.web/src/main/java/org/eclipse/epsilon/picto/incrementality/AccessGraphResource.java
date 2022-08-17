package org.eclipse.epsilon.picto.incrementality;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.Entity;
import org.eclipse.epsilon.picto.pictograph.InputEntity;
import org.eclipse.epsilon.picto.pictograph.Module;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.PictoGraph;
import org.eclipse.epsilon.picto.pictograph.PictographFactory;
import org.eclipse.epsilon.picto.pictograph.Property;
import org.eclipse.epsilon.picto.pictograph.Resource;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.State;
import org.eclipse.epsilon.picto.pictograph.Template;

public class AccessGraphResource implements AccessResource {

	private PictographFactory factory = PictographFactory.eINSTANCE;
	private PictoGraph graph = factory.createPictoGraph();
	private List<EMap<String, Entity>> entityMaps = new ArrayList<>();

	public AccessGraphResource() {
		entityMaps.add(graph.getPaths());
		entityMaps.add(graph.getModules());
		entityMaps.add(graph.getRules());
		entityMaps.add(graph.getTemplates());
		entityMaps.add(graph.getResources());
		entityMaps.add(graph.getElements());
		entityMaps.add(graph.getProperties());
	}

	@Override
	public void add(Access access) {
//		System.out.println(access.toString());

		// path
		if (access.getPath() == null) {
			return;
		}

		String pathName = access.getPath();
		Path path = (Path) graph.getPaths().get(pathName);
		if (path == null) {
			path = factory.createPath();
			path.setState(State.NEW);
			path.setName(access.getPath());
			graph.getPaths().put(pathName, path);
		}

		// module
		if (access.getModulePath() != null) {
			String moduleName = access.getModulePath();
			Module module = (Module) graph.getModules().get(moduleName);
			if (module == null) {
				module = factory.createModule();
				module.setState(State.NEW);
				module.setName(access.getModulePath());
				graph.getModules().put(moduleName, module);
			}
			addAffectedPath((InputEntity) module, path);

			// rule
			if (access.getGenerationRuleName() != null) {
				String ruleName = access.getGenerationRuleName();
				Rule rule = (Rule) graph.getRules().get(ruleName);
				if (rule == null) {
					rule = factory.createRule();
					rule.setState(State.NEW);
					rule.setName(access.getGenerationRuleName());
					graph.getRules().put(ruleName, rule);
				}
				rule.setModule(module);
				addAffectedPath((InputEntity) rule, path);

				// template
				if (access.getTemplatePath() != null) {
					String templateName = access.getTemplatePath();
					Template template = (Template) graph.getTemplates().get(templateName);
					if (template == null) {
						template = factory.createTemplate();
						template.setState(State.NEW);
						template.setName(access.getTemplatePath());
						graph.getTemplates().put(templateName, template);
					}
					rule.setTemplate(template);
					addAffectedPath((InputEntity) template, path);
				}

				// context element
				if (access.getContextObjectId() != null) {

					// element resource
					Resource contextResource = null;
					if (access.getContextResourceUri() != null) {
						String fullname = access.getContextResourceUri();
						contextResource = (Resource) graph.getResources().get(fullname);
						if (contextResource == null) {
							contextResource = factory.createResource();
							contextResource.setState(State.NEW);
							contextResource.setName(access.getContextResourceUri());
							graph.getResources().put(fullname, contextResource);
						}
						addAffectedPath((InputEntity) contextResource, path);
					}

					// context element
					String contextFullname = access.getContextResourceUri() + "#" + access.getContextObjectId();
					Element contextElement = (Element) graph.getElements().get(contextFullname);
					if (contextElement == null) {
						contextElement = factory.createElement();
						contextElement.setState(State.NEW);
						contextElement.setName(access.getContextObjectId());
						rule.getContextElements().add(contextElement);
						graph.getElements().put(contextFullname, contextElement);
					}
					contextElement.setResource(contextResource);
					addAffectedPath((InputEntity) contextElement, path);

				}

				// element
				if (access.getElementObjectId() != null) {

					// element resource
					Resource elementResource = null;
					if (access.getElementResourceUri() != null) {
						String fullname = access.getElementResourceUri();
						elementResource = (Resource) graph.getResources().get(fullname);
						if (elementResource == null) {
							elementResource = factory.createResource();
							elementResource.setState(State.NEW);
							elementResource.setName(access.getElementResourceUri());
							graph.getResources().put(fullname, elementResource);
						}
						addAffectedPath((InputEntity) elementResource, path);
					}

					// element
					String elementFullname = (access.getElementResourceUri() != null)
							? access.getElementResourceUri() + "#" + access.getElementObjectId()
							: access.getElementObjectId();
					Element element = (Element) graph.getElements().get(elementFullname);
					if (element == null) {
						element = factory.createElement();
						element.setState(State.NEW);
						element.setName(access.getElementObjectId());
						rule.getElements().add(element);
						graph.getElements().put(elementFullname, element);
					}
					element.setResource(elementResource);
					addAffectedPath((InputEntity) element, path);

					// property
					if (access.getPropertyName() != null) {
						if (access.getValue().equals("Dan#java.lang.String")) {
							System.console();
						}

						String fullname = (access.getElementResourceUri() != null)
								? access.getElementResourceUri() + "#" + access.getElementObjectId() + "#"
										+ access.getPropertyName()
								: access.getElementObjectId() + "#" + access.getPropertyName();
						Property property = (Property) graph.getProperties().get(fullname);
						if (property == null) {
							property = factory.createProperty();
							property.setState(State.NEW);
							property.setName(access.getPropertyName());
							graph.getProperties().put(fullname, property);
						}
						property.setElement(element);
						property.setValue(access.getValue());
						addAffectedPath((InputEntity) property, path);
					}
				}

			}
		}

	}

	/***
	 * 
	 */
	@Override
	public Set<String> getToBeProcessedPaths(List<LazyGenerationRuleContentPromise> inProcessingPromises,
			EgxModule module) {
		Set<String> toBeProcessedPaths = new HashSet<String>();
		Set<String> toBeDeletedKeys = new HashSet<String>();
		Set<EObject> toBeDeletedElements = new HashSet<EObject>();

		for (LazyGenerationRuleContentPromise promise : inProcessingPromises) {

			String checkedPath = Util.getPath(promise);

			if (checkedPath.equals("/Social Network/Dan")) {
				System.console();
			}

			Path path = (Path) graph.getPaths().get(checkedPath);
			// check if the path is a new view
			if (path == null) {
				toBeProcessedPaths.add(checkedPath);
				continue;
			} else {

				if (path.getState().equals(State.NEW)) {
					toBeProcessedPaths.add(checkedPath);

					Set<Entry<String, Entity>> affectingElements = graph.getElements().stream()
							.filter(e -> ((InputEntity) e.getValue()).getAffects().contains(path))
							.collect(Collectors.toSet());
					for (Entry<String, Entity> entry : affectingElements) {
						Element element = (Element) entry.getValue();
						toBeProcessedPaths.addAll(
								element.getAffects().stream().map(p -> p.getName()).collect(Collectors.toSet()));
					}
//					continue;
				}

				// check elements
//				Set<Entry<String, Entity>> affectingElements = graph.getElements().stream()
//						.filter(e -> ((InputEntity)e.getValue()).getAffects().contains(path)
//						).collect(Collectors.toSet()); 
//				for (Entry<String, Entity> entry : affectingElements) {

//					Element element = (Element) entry.getValue();
//
//					Resource resource = element.getResource();
//
//					if (resource == null) { // this one is for class operation, e.g., Person.all
//						continue;
//					}
//
//					String resourcePath = resource.getName();
//					EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream().filter(
//							m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(resourcePath))
//							.findFirst().orElse(null);
//
//					// if the resource has been deleted
//					if (!(new File(resource.getName())).exists()) {
//						toBeProcessedPaths.add(checkedPath);
//						toBeDeletedKeys.add(resource.getName());
//						toBeDeletedElements.add(resource);
//					} else if (model != null) {
//						org.eclipse.emf.ecore.resource.Resource currentResource = model.getResource();
//						EObject currentEObject = currentResource.getEObject(element.getName());
//						if (currentEObject == null) {
//							toBeProcessedPaths.add(checkedPath);
//							for (Property property : element.getProperties()) {
//								toBeDeletedKeys
//										.add(resource.getName() + "#" + element.getName() + "#" + property.getName());
//							}
//							toBeDeletedKeys.add(resource.getName() + "#" + element.getName());
//							toBeDeletedElements.add(element);
//						}
//					}
//				}
				// ----

				// check properties

				Set<Entry<String, Entity>> affectingProperties = graph.getProperties().stream()
						.filter(e -> ((InputEntity) e.getValue()).getAffects().contains(path))
						.collect(Collectors.toSet());

				for (Entry<String, Entity> entry : affectingProperties) {
					Property property = (Property) entry.getValue();
					Element element = property.getElement();
					Resource resource = element.getResource();
					if (resource == null) {
						continue;
					}
					String resourcePath = resource.getName();
					EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream().filter(
							m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(resourcePath))
							.findFirst().orElse(null);

					// if the resource has been deleted
					if (!(new File(resource.getName())).exists()) {
						toBeProcessedPaths.add(checkedPath);
						toBeDeletedKeys.add(resource.getName());
						toBeDeletedElements.add(resource);
					} else if (model != null) {
						org.eclipse.emf.ecore.resource.Resource currentResource = model.getResource();
						EObject currentEObject = currentResource.getEObject(element.getName());

						// if the element has been deleted
						if (currentEObject == null) {
							toBeProcessedPaths.add(checkedPath);
							for (Property p : element.getProperties()) {
								toBeDeletedKeys.add(resource.getName() + "#" + element.getName() + "#" + p.getName());
							}
							toBeDeletedKeys.add(resource.getName() + "#" + element.getName());
							toBeDeletedElements.add(currentEObject);
						} else {
							EStructuralFeature currentProperty = currentEObject.eClass()
									.getEStructuralFeature(property.getName());
							Object currentValueObject = (currentProperty != null) ? currentEObject.eGet(currentProperty)
									: null;

							// check if the property has been changed
							String currentValue = Access.convertValueToString(currentValueObject);
							if (!Util.equals(property.getPreviousValue(), currentValue)) {

								toBeProcessedPaths.addAll(element.getAffects().stream().map(p -> p.getName())
										.collect(Collectors.toSet()));

								toBeProcessedPaths.addAll(property.getAffects().stream().map(p -> p.getName())
										.collect(Collectors.toSet()));
							}

						}
					}
				}
			}
		}

		new Thread() {
			@Override
			public void run() {
				for (String key : toBeDeletedKeys) {
					for (EMap<String, Entity> map : entityMaps) {
						try {
							map.remove((Object) key);
						} catch (Exception e) {
						}
					}
				}

				for (EObject element : toBeDeletedElements) {
					try {
						EcoreUtil.delete(element, true);
					} catch (Exception e) {
					}
				}
			};
		}.start();
		return toBeProcessedPaths;
	}

	private void addAffectedPath(InputEntity module, final Path path) {
		if (!module.getAffects().stream().anyMatch(p -> p.getName().equals(path.getName()))) {
			module.getAffects().add(path);
		}
	}

	@Override
	public List<Access> getIncrementalRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printIncrementalRecords() {
		System.out.println();
		printEntities("## Modules ##", graph.getModules());
		printEntities("## Rules ##", graph.getRules());
		printEntities("## Templates ##", graph.getTemplates());
		printEntities("## Resources ##", graph.getResources());
		printEntities("## Elements ##", graph.getElements());
		printEntities("## Properties ##", graph.getProperties());
	}

	private void printEntities(String header, EMap<String, Entity> map) {
		System.out.println(header);
		for (Entity temp : map.values()) {
			InputEntity e1 = (InputEntity) temp;

			List<String> keys = new ArrayList<String>();
			InputEntity ie = e1;
			keys.add(ie.getName());
			while (ie.eContainer() != null) {
				ie = (InputEntity) ie.eContainer();
				keys.add(0, ie.getName());
			}
			System.out.print(String.join("#", keys) + " --> ");
			Set<String> paths = e1.getAffects().stream().map(p -> p.getName()).collect(Collectors.toSet());
			System.out.println(String.join(", ", paths));
		}
		System.out.println();
	}

//	@Override
//	public void updateStatusToProcessed(String path) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void updateStatusToProcessed(Collection<String> paths) {
		for (Entry<String, Entity> entry : graph.getPaths()) {
			Path p = (Path) entry.getValue();
			p.setState(State.PROCESSED);
			if (paths.contains(p.getName())) {
				for (Entity entity : p.getAffectedBy()) {
					entity.setState(State.PROCESSED);
					if (entity instanceof Property) {
						((Property) entity).setPreviousValue(((Property) entity).getValue());
					}
				}
			}
		}
	}

	@Override
	public void clear() {
		for (EMap<String, Entity> map : entityMaps) {
			map.clear();
			System.console();
		}
		for (EObject eObject : graph.eContents()) {
			EcoreUtil.delete(eObject, false);
		}
		System.console();
//		graph.eContents().clear();
	}

	@Override
	public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
			String path) {

	}

}