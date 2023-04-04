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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Property;

import net.sourceforge.plantuml.project.lang.SentenceIsDeleted;

public class AccessGraphResource implements AccessRecordResource {

//  private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors
//      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//  private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
  private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
      Runtime.getRuntime().availableProcessors(), 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
//  private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES,
//      new LinkedBlockingQueue<Runnable>());

  private Map<String, Path> paths = new LinkedHashMap<>();
  private Map<String, String> promises = new LinkedHashMap<>();

  public static ThreadPoolExecutor getExecutorService() {
    return executorService;
  }

  public int size() {
    return paths.size();
  }

  public Map<String, Path> getTraceIndex() {
    return paths;
  }

  public Map<String, String> getPromises() {
    return promises;
  }

  public void addAll(List<AccessRecord> currentPropertyAccesses) {
    currentPropertyAccesses.parallelStream().forEach(access -> {
      this.add(access);
    });
  }

  @SuppressWarnings("null")
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
          property = new Property(propertyId, access.getElementResourceUri(), access.getElementObjectId(),
              access.getPropertyName(), access.getValue());
          path.putProperty(propertyId, property);
        }

        // promise
        String promiseKey = access.getModulePath() + "#" + access.getGenerationRuleName() + "#"
            + access.getContextResourceUri() + "#" + access.getContextObjectId();
//        System.out.println("PK:" + promiseKey);
        String ps = promises.get(promiseKey);
        if (ps == null) {
          promises.put(promiseKey, pathName);
        }

      }
    };
    executorService.submit(t);

  }

  @Override
  public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
      EgxModule module) {

    Set<String> invalidatedPaths = ConcurrentHashMap.newKeySet();
    Set<String> toBeDeletedPaths = new HashSet<>();
    Set<String> toBeDeletedProperties = new HashSet<>();

//    for (IncrementalLazyGenerationRuleContentPromise promise : promises) {
//      checkPath(module, invalidatedPaths, toBeDeletedPaths, toBeDeletedProperties, promise);
//    }

    promises.parallelStream().forEach(promise -> {
      checkPath(module, invalidatedPaths, toBeDeletedPaths, toBeDeletedProperties, promise);
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
  public void checkPath(EgxModule module, Set<String> invalidatedPaths, Set<String> toBeDeletedPaths,
      Set<String> toBeDeletedProperties, IncrementalLazyGenerationRuleContentPromise promise) {
    String pathName = IncrementalityUtil.getPath(promise);
    this.checkPath(module, invalidatedPaths, toBeDeletedPaths, toBeDeletedProperties, pathName, true);
  }

  /***
   * 
   * @param module
   * @param invalidatedPaths
   * @param toBeDeletedPaths
   * @param toBeDeletedProperties
   * @param pathName
   */
  public void checkPath(EgxModule module, Set<String> invalidatedPaths, Set<String> toBeDeletedPaths,
      Set<String> toBeDeletedProperties, String pathName, boolean changeState) {

    // path
    Path path = paths.get(pathName);
    if (path == null) {
      invalidatedPaths.add(pathName);
      return;
    }
    if (path.isNew()) {
      invalidatedPaths.add(pathName);
      path.setOld();
    }

//    if (path.getName().equals("/Social Network")) {
//      this.printIncrementalRecords();
//      System.console();
//    }

    Iterator<Entry<String, Property>> iterator = path.getProperties().entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, Property> entry = iterator.next();
      Property property = entry.getValue();
      if (property.isDeleted()) {
        continue;
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
    // TODO Auto-generated method stub
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
