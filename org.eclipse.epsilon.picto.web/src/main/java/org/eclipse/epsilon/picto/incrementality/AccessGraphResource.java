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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.Entity;
import org.eclipse.epsilon.picto.pictograph.InputEntity;
import org.eclipse.epsilon.picto.pictograph.Module;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Property;
import org.eclipse.epsilon.picto.pictograph.Resource;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.State;
import org.eclipse.epsilon.picto.pictograph.Template;

public class AccessGraphResource implements AccessRecordResource {

  private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
//  private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1,
//      Runtime.getRuntime().availableProcessors(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

  private TraceIndex traceIndex = new TraceIndex();

  public static ThreadPoolExecutor getExecutorService() {
    return executorService;
  }

  public int size() {
    return traceIndex.size();
  }

  public TraceIndex getTraceIndex() {
    return traceIndex;
  }

  @SuppressWarnings("null")
  @Override
  public void add(AccessRecord access) {

    Thread t = new Thread("SaveToGraph-" + access.toString()) {

      @Override
      public void run() {

        // path
        if (access.getPath() == null) {
          return;
        }

        String pathName = access.getPath();
        Path path = (Path) traceIndex.getPath(pathName);
//        Path path = (Path) graph.getPaths().get(pathName);
        if (path == null) {
          path = new Path();
          path.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
          path.setName(access.getPath());
          traceIndex.putPath(pathName, path);
        }
        path.setAccessCount(path.getAccessCount() + 1);

        // module
        if (access.getModulePath() != null) {
          String moduleName = access.getModulePath();
          Module module = (Module) traceIndex.getModule(moduleName);
          if (module == null) {
            module = new Module();
            module.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
            module.setName(access.getModulePath());
            traceIndex.putModule(moduleName, module);
          }
          module.setAccessCount(module.getAccessCount() + 1);
          addAffectedPath((InputEntity) module, path);

          // rule
          if (access.getGenerationRuleName() != null) {
            String ruleName = access.getGenerationRuleName();
            Rule rule = (Rule) traceIndex.getRule(moduleName, ruleName);
            if (rule == null) {
              rule = new Rule();
              rule.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
              rule.setName(access.getGenerationRuleName());
              traceIndex.putRule(moduleName, ruleName, rule);
            }
            rule.setAccessCount(rule.getAccessCount() + 1);
            rule.setModule(module);
            addAffectedPath((InputEntity) rule, path);

            // template
            Template template = null;
            if (access.getTemplatePath() != null) {
              String templateName = access.getTemplatePath();
              template = (Template) traceIndex.getTemplate(moduleName, ruleName, templateName);
              if (template == null) {
                template = new Template();
                template.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                template.setName(access.getTemplatePath());
                traceIndex.putTemplate(moduleName, ruleName, templateName, template);
              }
              template.setAccessCount(template.getAccessCount() + 1);
              template.addModule(module);
              rule.setTemplate(template);
              addAffectedPath((InputEntity) template, path);

            }

            // context element
            if (access.getContextObjectId() != null) {

              // element resource
              Resource contextResource = null;
              if (access.getContextResourceUri() != null) {
                String fullname = access.getContextResourceUri();
                contextResource = (Resource) traceIndex.getResource(fullname);
                if (contextResource == null) {
                  contextResource = new Resource();
                  contextResource.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                  contextResource.setName(access.getContextResourceUri());
                  traceIndex.putResource(fullname, contextResource);
                }
                contextResource.setAccessCount(contextResource.getAccessCount() + 1);
                addAffectedPath((InputEntity) contextResource, path);
              }

              // context element
              Element contextElement = (Element) traceIndex.getElement(access.getContextResourceUri(),
                  access.getContextObjectId());
              if (contextElement == null) {
                contextElement = new Element();
                contextElement.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                contextElement.setName(access.getContextObjectId());
                rule.getContextElements().add(contextElement);
                traceIndex.putElement(access.getContextResourceUri(), access.getContextObjectId(), contextElement);
              }
              contextElement.setAccessCount(contextElement.getAccessCount() + 1);
              contextElement.setResource(contextResource);
              addAffectedPath((InputEntity) contextElement, path);

            }

            // element
            if (access.getElementObjectId() != null) {

              // element resource
              Resource elementResource = null;
              if (access.getElementResourceUri() != null) {
                String fullname = access.getElementResourceUri();
                elementResource = (Resource) traceIndex.getResource(fullname);
                if (elementResource == null) {
                  elementResource = new Resource();
                  elementResource.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                  elementResource.setName(access.getElementResourceUri());
                  traceIndex.putResource(fullname, elementResource);
                }
                elementResource.setAccessCount(elementResource.getAccessCount() + 1);
                addAffectedPath((InputEntity) elementResource, path);
              }

              // element
              Element element = (Element) traceIndex.getElement(access.getElementResourceUri(),
                  access.getElementObjectId());
              if (element == null) {
                element = new Element();
                element.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                element.setName(access.getElementObjectId());
                rule.getElements().add(element);
                traceIndex.putElement(access.getElementResourceUri(), access.getElementObjectId(), element);
              }
              element.setAccessCount(element.getAccessCount() + 1);
              template.addelement(element);
              element.setResource(elementResource);
              addAffectedPath((InputEntity) element, path);

              // property
              if (access.getPropertyName() != null) {

                Property property = (Property) traceIndex.getProperty(access.getElementResourceUri(),
                    access.getElementObjectId(), access.getPropertyName());
                if (property == null) {
                  property = new Property();
                  property.setState(org.eclipse.epsilon.picto.pictograph.State.NEW);
                  property.setName(access.getPropertyName());
                  traceIndex.putProperty(access.getElementResourceUri(), access.getElementObjectId(),
                      access.getPropertyName(), property);
                }
                property.setAccessCount(property.getAccessCount() + 1);
                property.setElement(element);
                property.setValue(access.getValue());
                addAffectedPath((InputEntity) property, path);
              }
            }

          }
        }
//        System.out.println(access.toString());
      }
    };
    executorService.submit(t);

  }

  @Override
  public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
      EgxModule module) {
    Set<String> invalidatedViewPaths = Collections.synchronizedSet(new HashSet<String>());
    Set<String> toBeDeletedKeys = Collections.synchronizedSet(new HashSet<String>());
    Set<Entity> toBeDeletedElements = Collections.synchronizedSet(new HashSet<Entity>());

    for (IncrementalLazyGenerationRuleContentPromise promise : promises) {
      String checkedPath = IncrementalityUtil.getPath(promise);
      checkPath(module, invalidatedViewPaths, toBeDeletedKeys, toBeDeletedElements, checkedPath);
    }

//    promises.parallelStream().forEach(promise -> {
//      String checkedPath = IncrementalityUtil.getPath(promise);
//      checkPath(module, invalidatedViewPaths, toBeDeletedKeys, toBeDeletedElements, checkedPath);
//    });

    // run a new thread in the background to remove deleted elements from the
    // indices and graph
    Thread t = new Thread() {
      public void run() {

        // first, remove all the entries of the deleted elements
        for (String key : toBeDeletedKeys) {
          for (Map<String, ?> map : traceIndex.getAllIndices()) {
            try {
              map.remove((Object) key);
            } catch (Exception e) {
            }
          }
        }

        for (Entity element : toBeDeletedElements) {
          try {
            InputEntity ie = (InputEntity) element;
            List<Path> paths = ie.getAffects();
            for (Path path : paths) {
              while (path.getAffectedBy().remove(ie))
                ;
            }
//            EcoreUtil.delete(element, true);
          } catch (Exception e) {
          }
        }

        Iterator<Entry<String, Path>> iterator = traceIndex.getPathIndex().entrySet().iterator();
        while (iterator.hasNext()) {
          Path p = iterator.next().getValue();
          if (p.getAffectedBy().size() == 0) {
            iterator.remove();
          }
        }
      }
    };
    t.setName("PictoGraph Cleaner Thread");
    t.start();

    return invalidatedViewPaths;
  }

  /**
   * check if a path is affected by changes
   * 
   * @param module
   * @param toBeProcessedPaths
   * @param toBeDeletedKeys
   * @param toBeDeletedEntities
   * @param checkedPath
   */
  protected void checkPath(EgxModule module, Set<String> toBeProcessedPaths, Set<String> toBeDeletedKeys,
      Set<Entity> toBeDeletedEntities, String checkedPath) {

//    int numCheckedProperties = 0;

    long start = System.currentTimeMillis();
    Path path = (Path) traceIndex.getPath(checkedPath);
//    System.out.println("CHECKING PATH: " + checkedPath);
    if ("/Stats".equals(checkedPath)) {
      System.console();
    }

    // check if the path is a new view
    if (path == null) {
      toBeProcessedPaths.add(checkedPath);
      return;
    } else {
      path.setCheckCount(path.getCheckCount() + 1);

      if (path.getState().equals(State.NEW)) {
        toBeProcessedPaths.add(checkedPath);
        long time = System.currentTimeMillis() - start;
        path.setCheckingTime(path.getCheckingTime() + time);
        path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());
        return;
      }

      // check if the new path is affected by a new object
      int i = 0;
      while (i < path.getAffectedBy().size()) {
        List<InputEntity> list = path.getAffectedBy();
        if (list == null) {
          break;
        }
        InputEntity entity = list.get(i);
        i++;
//      for (InputEntity entity : path.getAffectedBy()) {

        if (entity instanceof Element) {
          if (entity.getState().equals(State.NEW)) {
            toBeProcessedPaths.add(checkedPath);
            long time = System.currentTimeMillis() - start;
            path.setCheckingTime(path.getCheckingTime() + time);
            path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());
            return;
          }
        }
        // property
        else if (entity instanceof Property) {
          Property property = (Property) entity;
          Element element = property.getElement();
          if (element == null) {
            continue;
          }
          Resource resource = element.getResource();
          if (resource == null) {
            continue;
          }
          String resourcePath = resource.getName();

          /** this code is quite expensive, should be optimised */

//          EmfModel model = null;
//          for (IModel m : module.getContext().getModelRepository().getModels()) {
//            if (((AbstractEmfModel) m).getResource().getURI().toFileString().equals(resourcePath)){
//              model = (EmfModel) m;
//              break;
//            }
//          }

          EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream()
              .filter(m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(resourcePath))
              .findFirst().orElse(null);

          // if the resource has been deleted
//          if (!(new File(resource.getName())).exists()) {
////            toBeProcessedPaths.add(checkedPath);
////            toBeDeletedKeys.add(resource.getName());
////            toBeDeletedElements.add(resource);
////
////            long time = System.currentTimeMillis() - start;
////            path.setCheckingTime(path.getCheckingTime() + time);
////            path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());
//
//            return;
//          } 
//          else 
          if (model != null) {
            org.eclipse.emf.ecore.resource.Resource currentResource = model.getResource();
            EObject currentEObject = currentResource.getEObject(element.getName());

            // if the element has been deleted
            if (currentEObject == null) {
              toBeProcessedPaths.add(checkedPath);

              long time = System.currentTimeMillis() - start;
              path.setCheckingTime(path.getCheckingTime() + time);
              path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());

              for (Property p : element.getProperties()) {
                toBeDeletedKeys.add(resource.getName() + "#" + element.getName() + "#" + p.getName());
                toBeDeletedEntities.add(p);
              }
              toBeDeletedKeys.add(resource.getName() + "#" + element.getName());
              toBeDeletedEntities.add(element);

              return;
            } else {

//              numCheckedProperties += 1;

              EStructuralFeature currentProperty = currentEObject.eClass().getEStructuralFeature(property.getName());
              Object currentValueObject = (currentProperty != null) ? currentEObject.eGet(currentProperty) : null;

              // check if the property has been changed
              String currentValue = AccessRecord.convertValueToString(currentValueObject);
//              String previousValue = property.getValue();

              if (!IncrementalityUtil.equals(property.getPreviousValue(), currentValue)) {
//              if (!IncrementalityUtil.equals(previousValue, currentValue)) {
//                System.out.println(checkedPath + ": " + currentEObject.eClass().getName() + " - " + property.getName()
//                    + ": " + property.getPreviousValue() + " vs. " + currentValue);
                toBeProcessedPaths.add(checkedPath);
                property.setValue(currentValue);

                long time = System.currentTimeMillis() - start;
                path.setCheckingTime(path.getCheckingTime() + time);
                path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());

//                /** Record the number of Properties checked **/
//                PerformanceRecord r = new PerformanceRecord(PerformanceRecorder.genenerateAll,
//                    PerformanceRecorder.generateAlways, PerformanceRecorder.globalNumberOfAffectedViews,
//                    PerformanceRecorder.globalNumberIteration, "Server", checkedPath, numCheckedProperties, 0,
//                    PerformanceTestType.CHECKED_PROPERTIES, PerformanceRecorder.accessRecordResourceSize());
//                PerformanceRecorder.record(r);

//                int count = 0;
//                int it = 0;
//                while (it < path.getAffectedBy().size()) {
//                  Object o = path.getAffectedBy().get(it);
//                  if (o instanceof Property) {
//                    count++;
//                  }
//                  it++;
//                }
//
//                r = new PerformanceRecord(PerformanceRecorder.genenerateAll, PerformanceRecorder.generateAlways,
//                    PerformanceRecorder.globalNumberOfAffectedViews, PerformanceRecorder.globalNumberIteration,
//                    "Server", checkedPath, count, 0, PerformanceTestType.PROPERTIES,
//                    PerformanceRecorder.accessRecordResourceSize());
//                PerformanceRecorder.record(r);

//                property.getAffects().forEach(p -> toBeProcessedPaths.add(p.getName()));
                return;
              }

            }
          }
        }
      }

//      /** Record the number of Properties checked **/
//      PerformanceRecord r = new PerformanceRecord(PerformanceRecorder.genenerateAll, PerformanceRecorder.generateAlways,
//          PerformanceRecorder.globalNumberOfAffectedViews, PerformanceRecorder.globalNumberIteration, "Server",
//          checkedPath, numCheckedProperties, 0, PerformanceTestType.CHECKED_PROPERTIES,
//          PerformanceRecorder.accessRecordResourceSize());
//      PerformanceRecorder.record(r);
//
//      r = new PerformanceRecord(PerformanceRecorder.genenerateAll, PerformanceRecorder.generateAlways,
//          PerformanceRecorder.globalNumberOfAffectedViews, PerformanceRecorder.globalNumberIteration, "Server",
//          checkedPath, numCheckedProperties, 0, PerformanceTestType.PROPERTIES,
//          PerformanceRecorder.accessRecordResourceSize());
//      PerformanceRecorder.record(r);
//
      long time = System.currentTimeMillis() - start;
      path.setCheckingTime(path.getCheckingTime() + time);
      path.setAvgCheckTime(path.getCheckingTime() / path.getCheckCount());
    }
  }

  /***
   * Add the affected path to the affecting module.
   * 
   * @param inputEntity
   * @param path
   */
  private void addAffectedPath(InputEntity inputEntity, final Path path) {
    List<Path> affectedPaths = inputEntity.getAffects();
    for (int i = 0; i < affectedPaths.size(); i++) {
      if (i < affectedPaths.size()) {
        Path affectedPath = affectedPaths.get(i);
        if (path.getName().equals(affectedPath.getName()))
          return;
      }
    }
//    inputEntity.getAffects().add(path);
    inputEntity.addAffectedPath(path);
//    if (!module.getAffects().stream().anyMatch(p -> p.getName().equals(path.getName()))) {
//      module.getAffects().add(path);
//    }
  }

  @Override
  public List<AccessRecord> getIncrementalRecords() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void printIncrementalRecords() {
    System.out.println();
    printEntities("## Modules ##", traceIndex.getModuleIndex());
    printEntities("## Rules ##", traceIndex.getRuleIndex());
    printEntities("## Templates ##", traceIndex.getTemplateIndex());
    printEntities("## Resources ##", traceIndex.getResourceIndex());
    printEntities("## Elements ##", traceIndex.getElementIndex());
    printEntities("## Properties ##", traceIndex.getPropertyIndex());
  }

  private void printEntities(String header, Map<String, ?> map) {
    System.out.println(header);
    for (Object temp : map.values()) {
      InputEntity e1 = (InputEntity) temp;

      List<String> keys = new ArrayList<String>();
      InputEntity ie = e1;
      keys.add(ie.getName());
//      while (ie.eContainer() != null) {
//        ie = (InputEntity) ie.eContainer();
//        keys.add(0, ie.getName());
//      }
//      System.out.print(String.join("#", keys) + " --> ");
//      Set<String> paths = e1.getAffects().stream().map(p -> p.getName()).collect(Collectors.toSet());
//      System.out.println(String.join(", ", paths));
    }
    System.out.println();
  }

  @Override
  public void updateStatusToProcessed(Collection<String> paths) {
//    Thread t = new Thread("UpdatePathStatus") {
//      public void run() {

    for (String path : paths) {
      Path p = traceIndex.getPath(path);
      if (p == null)
        continue;
      p.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
      for (int i = 0; i < p.getAffectedBy().size(); i++) {
        Entity entity = p.getAffectedBy().get(i);
        entity.setState(org.eclipse.epsilon.picto.pictograph.State.PROCESSED);
        if (entity instanceof Property) {
          Property property = (Property) entity;
          property.setPreviousValue(property.getValue());
          System.console();
        }
      }
    }

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
    for (Map<String, ?> map : traceIndex.getAllIndices()) {
      map.clear();
      System.console();
    }
//    for (EObject eObject : graph.eContents()) {
//      EcoreUtil.delete(eObject, false);
//    }
    System.console();
//		graph.eContents().clear();
  }

  @Override
  public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
      String path) {

  }

  @Override
  public State getPathStatus(String pathString) {
    Path path = traceIndex.getPath(pathString);
    if (path != null) {
      return traceIndex.getPath(pathString).getState();
    } else {
      return null;
    }
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
