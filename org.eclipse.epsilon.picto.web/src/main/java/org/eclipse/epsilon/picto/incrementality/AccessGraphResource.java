/*********************************************************************
* Copyright (c) 2022 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Property;

/***
 * 
 * @author alfa
 *
 */
public class AccessGraphResource implements AccessRecordResource {

	private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
			Runtime.getRuntime().availableProcessors(), 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());

	private Map<String, Path> paths = new LinkedHashMap<>();
	private Map<String, String> moduleRuleObjectIdsToPaths = new LinkedHashMap<>();

	private Set<String> deletedPaths = new HashSet<>();

	public static ThreadPoolExecutor getExecutorService() {
		return executorService;
	}

	public Set<String> getDeletedPaths() {
		return deletedPaths;
	}

	public int size() {
		return paths.size();
	}

	public Map<String, Path> getPaths() {
		return paths;
	}

	public Map<String, String> getPromiseKeysToPaths() {
		return moduleRuleObjectIdsToPaths;
	}

	public void addAll(List<AccessRecord> currentPropertyAccesses) {
		currentPropertyAccesses.parallelStream().forEach(access -> {
			this.add(access);
		});
	}

	@Override
	public void add(AccessRecord access) {

		
		Thread t = new Thread("SaveToGraph-" + access.toString()) {

			@Override
			public void run() {
				
//				System.out.println(access.toString());

				// skip
				if (access.getPath() == null || access.getPropertyName() == null || access.getElementResourceUri() == null
						|| access.getElementObjectId() == null) {
					return;
				}

				// path
				String pathName = access.getPath();
				Path path = paths.get(pathName);
				if (path == null) {
					path = new Path();
					path.setName(pathName);
					paths.put(pathName, path);
				}

				// property
				String propertyId = access.getElementResourceUri() + "#" + access.getElementObjectId() + "#"
						+ access.getPropertyName();

				Property property = path.getProperty(propertyId);
				if (property == null) {
					property = new Property(propertyId, access.getContextResourceUri(), access.getContextObjectId(),
							access.getElementResourceUri(), access.getElementObjectId(), access.getPropertyName(), access.getValue());
					path.putProperty(propertyId, property);
				}

				// Promise
				String moduleRuleObjectId = access.getModulePath() + "#" + access.getGenerationRuleName() + "#"
						+ access.getContextResourceUri() + "#" + access.getContextObjectId();
				String ps = moduleRuleObjectIdsToPaths.get(moduleRuleObjectId);
				if (ps == null) {
					moduleRuleObjectIdsToPaths.put(moduleRuleObjectId, pathName);
				}

			}
		};
		executorService.submit(t);

	}

	@Override
	public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
			EgxModule module) {

		Set<String> invalidatedPaths = ConcurrentHashMap.newKeySet();

		promises.parallelStream().forEach(promise -> {
			checkPath(module, invalidatedPaths, promise);
		});

		return invalidatedPaths;

	}

	/***
	 * 
	 * @param module
	 * @param invalidatedPaths
	 * @param toBeDeletedPaths
	 * @param toBeDeletedProperties
	 * @param promise
	 */
	public void checkPath(EgxModule module, Set<String> invalidatedPaths,
			IncrementalLazyGenerationRuleContentPromise promise) {
		String pathName = IncrementalityUtil.getPath(promise);
		this.checkPath(module, invalidatedPaths, pathName, true);
	}

	/***
	 * 
	 * @param module
	 * @param invalidatedPaths
	 * @param toBeDeletedPaths
	 * @param toBeDeletedProperties
	 * @param pathName
	 */
	public void checkPath(EgxModule module, Set<String> invalidatedPaths, String pathName, boolean changeState) {

		// path
		//System.out.println("ALFA: " + pathName);
		if (pathName.equals("/Social Network/Alice")) {
			System.console();
		}
		
		Path path = paths.get(pathName);
		if (path == null) {
			invalidatedPaths.add(pathName);
			return;
		}


		if (path.isNew()) {
			invalidatedPaths.add(pathName);
			path.setOld();
			return;
		}

		Iterator<Entry<String, Property>> iterator = path.getProperties().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Property> entry = iterator.next();
			Property property = entry.getValue();
			if (property.isDeleted()) {
//				continue;
			}

			// context resource
			Resource contextResource = null;
			EmfModel contextEmfModel = ((EmfModel) module.getContext().getModelRepository().getModels().stream()
					.filter(
							m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(property.getContextResource()))
					.findFirst().orElse(null));
			if (contextEmfModel != null) {

				contextResource = contextEmfModel.getResource();
				if (contextResource == null) {
					invalidatedPaths.add(pathName);
					path.setDeleted();
					property.setDeleted();
					deletedPaths.add(pathName);
//					continue;
				}

				// context eObject
				EObject eContextObject = contextResource.getEObject(property.getContextElement());
				if (eContextObject == null) {
					invalidatedPaths.add(pathName);
					path.setDeleted();
					property.setDeleted();
					deletedPaths.add(pathName);
//					continue;
				}
			}

			// resource
			Resource resource = ((EmfModel) module.getContext().getModelRepository().getModels().stream()
					.filter(m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(property.getResource()))
					.findFirst().orElse(null)).getResource();
			if (resource == null) {
				invalidatedPaths.add(pathName);
				property.setDeleted();
				continue;
			}

			// eObject
			EObject eObject = resource.getEObject(property.getElement());
			if (eObject == null) {
				invalidatedPaths.add(pathName);
				property.setDeleted();
				continue;
			}

			// property
			EStructuralFeature feature = eObject.eClass().getEStructuralFeature(property.getPropertyName());
			Object currentValueObject = (feature != null) ? eObject.eGet(feature) : null;
			String currentValue = AccessRecord.convertValueToString(currentValueObject);
			if (!IncrementalityUtil.equals(currentValue, property.getValue())) {
				invalidatedPaths.add(pathName);

				String strVal = property.getValue();
				System.console();

				// check if the value
				if (contextResource != null && feature.getEType() instanceof EObject) {
					if (feature.isMany()) {
						String temp = strVal.split("#")[0];
						temp = temp.substring(1, temp.length() - 1);
						String[] idsTypes = temp.split(",");
						for (String idType : idsTypes) {
							String id = idType.split(":")[0];
							EObject obj = ((XMIResource) contextResource).getEObject(id);
							if (obj == null) {

								String uri = resource.getURI().toFileString();
								List<String> paths = moduleRuleObjectIdsToPaths.entrySet().parallelStream()
										.filter(e -> e.getKey().contains(uri) && e.getKey().contains(id)).map(e -> e.getValue())
										.collect(Collectors.toList());
								
								deletedPaths.addAll(paths);
								
								paths.parallelStream().forEach(p -> {
									AccessGraphResource.this.paths.get(p).setDeleted();
								});
								
								invalidatedPaths.addAll(paths);
							}
						}
					} else if (strVal != null) {
						String id = strVal.split("#")[0];
						EObject obj = ((XMIResource) contextResource).getEObject(id);
						if (obj == null) {

							String uri = resource.getURI().toFileString();
							List<String> paths = moduleRuleObjectIdsToPaths.entrySet().parallelStream()
									.filter(e -> e.getKey().contains(uri) && e.getKey().contains(id)).map(e -> e.getValue())
									.collect(Collectors.toList());
							deletedPaths.addAll(paths);
							
							paths.parallelStream().forEach(p -> {
								AccessGraphResource.this.paths.get(p).setDeleted();
							});
							
							invalidatedPaths.addAll(paths);
						}
					}
				}

				// should the current value replace the previous value in the property
				if (changeState) {
					property.setValue(currentValue);
				}
			}

			if (property.isNew()) {
				property.setOld();
			}
		}

	}

	@Override
	public List<AccessRecord> getIncrementalRecords() {
		return null;
	}

	@Override
	public void printIncrementalRecords() {
		System.out.println();
		synchronized (paths) {
			for (Entry<String, Path> entry : paths.entrySet()) {
				System.out.println(entry.getKey());
				Map<String, Property> properties = entry.getValue().getProperties();
				synchronized (properties) {
					for (Property property : properties.values()) {
						System.out.print("+---");
						System.out.println(property);
					}
				}
			}
		}
	}

	@Override
	public void updateStatusToProcessed(Collection<String> paths) {

	}

	@Override
	public void clear() {
		for (Path path : paths.values()) {
			path.getProperties().clear();
		}
		paths.clear();
	}

	@Override
	public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
			String path) {

	}

	@Override
	public boolean getPathStatus(String pathString) {
		Path path = paths.get(pathString);
		return (path != null) ? path.isNew() : false;
	}

	class RecordTask implements Callable<Object> {

		AccessRecord access;

		/**
		 * 
		 */
		public RecordTask(AccessRecord access) {
			this.access = access;
		}

		@Override
		public Object call() throws Exception {

			return null;
		}

	}
}
