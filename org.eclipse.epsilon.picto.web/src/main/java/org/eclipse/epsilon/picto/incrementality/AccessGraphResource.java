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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
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

	public static ThreadPoolExecutor getExecutorService() {
		return executorService;
	}

	public int size() {
		return paths.size();
	}

	public Map<String, Path> getTraceIndex() {
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
				continue;
			}

			// context resource
			EmfModel contextEmfModel = ((EmfModel) module.getContext().getModelRepository().getModels().stream()
					.filter(
							m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(property.getContextResource()))
					.findFirst().orElse(null));
			if (contextEmfModel != null) {

				Resource contextResource = contextEmfModel.getResource();
				if (contextResource == null) {
					invalidatedPaths.add(pathName);
					path.setDeleted();
					property.setDeleted();
					continue;
				}

				// context eObject
				EObject eContextObject = contextResource.getEObject(property.getContextElement());
				if (eContextObject == null) {
					invalidatedPaths.add(pathName);
					path.setDeleted();
					property.setDeleted();
					continue;
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

	/***
	 * Remove all paths and properties that are flagged deleted.
	 */
	public void clean() {

		Thread t = new Thread("RemoveDeletedPathsAndProperties") {
			public void run() {
				System.out.print("Removing deleted paths and properties ... ");
				while (AccessGraphResource.getExecutorService().getQueue().size() > 0
						&& AccessGraphResource.getExecutorService().getActiveCount() > 0) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (AccessGraphResource.getExecutorService().getQueue().size() == 0
							&& AccessGraphResource.getExecutorService().getActiveCount() == 0) {

						// remove deleted properties in each path
						AccessGraphResource.this.paths.values().parallelStream().forEach(path -> {
							Iterator<Entry<String, Property>> propertyIterator = path.getProperties().entrySet().iterator();
							while (propertyIterator.hasNext()) {
								Entry<String, Property> propertyEntry = propertyIterator.next();
								Property property = propertyEntry.getValue();
								if (property.isDeleted())
									propertyIterator.remove();
							}
						});

						// remove deleted paths
						Iterator<Entry<String, Path>> pathIterator = AccessGraphResource.this.paths.entrySet().iterator();
						while (pathIterator.hasNext()) {
							Entry<String, Path> pathEntry = pathIterator.next();
							Path path = pathEntry.getValue();
							if (path.isDeleted())
								pathIterator.remove();
						}
					} // if
				} // while

				System.out.println("Done");

			} // run
		};
		t.start();
		executorService.submit(t);
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
