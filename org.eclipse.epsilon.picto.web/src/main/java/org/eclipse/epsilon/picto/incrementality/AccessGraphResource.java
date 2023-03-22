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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Property;

public class AccessGraphResource implements AccessRecordResource {

//  private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors
//      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//  private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
  private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
      Runtime.getRuntime().availableProcessors(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

//  private ConcurrentHashMap<String, Path> paths = new ConcurrentHashMap<>();
  private Map<String, Path> paths = new HashMap<>();

  Map<String, Resource> modelCache = new HashMap<>();
  Map<String, EObject> eObjectCache = new HashMap<>();
  Map<String, EStructuralFeature> featureCache = new HashMap<>();

  public static ThreadPoolExecutor getExecutorService() {
    return executorService;
  }

  public int size() {
    return paths.size();
  }

//  public ConcurrentHashMap<String, Path> getTraceIndex() {
  public Map<String, Path> getTraceIndex() {
    return paths;
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

      }
    };
    executorService.submit(t);

  }

  @Override
  public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
      EgxModule module) {
//    Set<String> invalidatedPaths = ConcurrentHashMap.newKeySet();
//    Set<String> toBeDeletedPaths = ConcurrentHashMap.newKeySet();
//    Set<String> toBeDeletedProperties = ConcurrentHashMap.newKeySet();
//
//    ConcurrentHashMap<String, Resource> modelCache = new ConcurrentHashMap<>();
//    ConcurrentHashMap<String, EObject> eObjectCache = new ConcurrentHashMap<>();
//    ConcurrentHashMap<String, EStructuralFeature> featureCache = new ConcurrentHashMap<>();

    Set<String> invalidatedPaths = new HashSet<>();
    Set<String> toBeDeletedPaths = new HashSet<>();
    Set<String> toBeDeletedProperties = new HashSet<>();
    
    for (IncrementalLazyGenerationRuleContentPromise promise : promises) {
      checkPath(module, invalidatedPaths, toBeDeletedPaths, toBeDeletedProperties, modelCache, eObjectCache,
          featureCache, promise);
    }

//    promises.parallelStream().forEach(promise -> {
//      checkPath(module, invalidatedPaths, toBeDeletedPaths, toBeDeletedProperties, modelCache, eObjectCache,
//          featureCache, promise);
//    });
    
    modelCache.clear();
    eObjectCache.clear();
    featureCache.clear();
    System.gc();

    return invalidatedPaths;
  }

//  public void checkPath(EgxModule module, Set<String> invalidatedPaths, Set<String> toBeDeletedPaths,
//      Set<String> toBeDeletedProperties, ConcurrentHashMap<String, Resource> modelCache,
//      ConcurrentHashMap<String, EObject> eObjectCache, ConcurrentHashMap<String, EStructuralFeature> featureCache,
//      IncrementalLazyGenerationRuleContentPromise promise) {
  public void checkPath(EgxModule module, Set<String> invalidatedPaths, Set<String> toBeDeletedPaths,
      Set<String> toBeDeletedProperties, Map<String, Resource> modelCache, Map<String, EObject> eObjectCache,
      Map<String, EStructuralFeature> featureCache, IncrementalLazyGenerationRuleContentPromise promise) {

    // path
    String pathName = IncrementalityUtil.getPath(promise);
    Path path = paths.get(pathName);
    if (path == null) {
      invalidatedPaths.add(pathName);
      return;
    }
    if (path.isNew()) {
      invalidatedPaths.add(pathName);
      if (pathName.equals("/Social Network/Alice")) {
        System.console();
      }
      path.setOld();
    }

    Iterator<Entry<String, Property>> iterator = path.getProperties().entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, Property> entry = iterator.next();
      Property property = entry.getValue();

      // resource
      Resource resource = modelCache.get(property.getResource());
      if (resource == null) {
        resource = ((EmfModel) module.getContext().getModelRepository().getModels().stream()
            .filter(m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(property.getResource()))
            .findFirst().orElse(null)).getResource();
        if (resource == null) {
          toBeDeletedPaths.add(pathName);
          continue;
        }
        modelCache.put(property.getResource(), resource);
      }

      // eObject
      String eObjectKey = property.getResource() + "#" + property.getElement();
      EObject eObject = eObjectCache.get(eObjectKey);
      if (eObject == null) {
        eObject = resource.getEObject(property.getElement());
        if (eObject == null) {
          invalidatedPaths.add(pathName);
          if (pathName.equals("/Social Network/Alice")) {
            System.console();
          }
          iterator.remove();
          continue;
        }
        eObjectCache.put(eObjectKey, eObject);
      }

      // property
      String featureKey = property.getResource() + "#" + property.getElement() + "#" + property.getPropertyName();
      EStructuralFeature feature = featureCache.get(featureKey);
      if (feature == null) {
        feature = eObject.eClass().getEStructuralFeature(property.getPropertyName());
        featureCache.put(featureKey, feature);
      }
      Object currentValueObject = (feature != null) ? eObject.eGet(feature) : null;
      String currentValue = AccessRecord.convertValueToString(currentValueObject);
      if (!IncrementalityUtil.equals(currentValue, property.getValue())) {
        invalidatedPaths.add(pathName);
        if (pathName.equals("/Social Network/Alice")) {
          System.console();
        }
        property.setValue(currentValue);
      }

      if (property.isNew()) {
        property.setOld();
      }
    }

//    deleteProperties(path, toBeDeletedProperties);

  }

//  private void deleteProperties(Path path, Set<String> toBeDeletedProperties) {
//    Thread t = new Thread("Delete Properties") {
//      public void run() {
//        deleteProperties(path, toBeDeletedProperties);
//      }
//    };
//    t.start();
//  }

  /***
   * Add the affected path to the affecting module.
   * 
   * @param inputEntity
   * @param path
   */
//  private void addAffectedPath(InputEntity inputEntity, final Path path) {
//    List<Path> affectedPaths = inputEntity.getAffects();
//    for (int i = 0; i < affectedPaths.size(); i++) {
//      if (i < affectedPaths.size()) {
//        Path affectedPath = affectedPaths.get(i);
//        if (path.getName().equals(affectedPath.getName()))
//          return;
//      }
//    }
////    inputEntity.getAffects().add(path);
//    inputEntity.addAffectedPath(path);
////    if (!module.getAffects().stream().anyMatch(p -> p.getName().equals(path.getName()))) {
////      module.getAffects().add(path);
////    }
//  }

  @Override
  public List<AccessRecord> getIncrementalRecords() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void printIncrementalRecords() {
//    System.out.println();
//    printEntities("## Modules ##", traceIndex.getModuleIndex());
//    printEntities("## Rules ##", traceIndex.getRuleIndex());
//    printEntities("## Templates ##", traceIndex.getTemplateIndex());
//    printEntities("## Resources ##", traceIndex.getResourceIndex());
//    printEntities("## Elements ##", traceIndex.getElementIndex());
//    printEntities("## Properties ##", traceIndex.getPropertyIndex());
  }

  private void printEntities(String header, Map<String, ?> map) {
//    System.out.println(header);
//    for (Object temp : map.values()) {
//      InputEntity e1 = (InputEntity) temp;
//
//      List<String> keys = new ArrayList<String>();
//      InputEntity ie = e1;
//      keys.add(ie.getName());
////      while (ie.eContainer() != null) {
////        ie = (InputEntity) ie.eContainer();
////        keys.add(0, ie.getName());
////      }
////      System.out.print(String.join("#", keys) + " --> ");
////      Set<String> paths = e1.getAffects().stream().map(p -> p.getName()).collect(Collectors.toSet());
////      System.out.println(String.join(", ", paths));
//    }
//    System.out.println();
  }

  @Override
  public void updateStatusToProcessed(Collection<String> paths) {
////    Thread t = new Thread("UpdatePathStatus") {
////      public void run() {
//
//    for (String path : paths) {
//      Path p = traceIndex.getPath(path);
//      if (p == null)
//        continue;
//      p.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
//      for (int i = 0; i < p.getAffectedBy().size(); i++) {
//        Entity entity = p.getAffectedBy().get(i);
//        entity.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
//        if (entity instanceof Property) {
//          Property property = (Property) entity;
//          property.setPreviousValue(property.getValue());
//          System.console();
//        }
//      }
//    }

//        for (Entry<String, Path> entry : traceIndex.getPathIndex().entrySet()) {
//          Path p = (Path) entry.getValue();
//          p.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
//          if (paths.contains(p.getName())) {
//            for (int i = 0; i < p.getAffectedBy().size(); i++) {
//              Entity entity = p.getAffectedBy().get(i);
//              entity.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
//              if (entity instanceof Property) {
//                ((Property) entity).setPreviousValue(((Property) entity).getValue());
//              }
//            }
//          }
//        }

//      }
//    };
//    executorService.submit(t);
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
