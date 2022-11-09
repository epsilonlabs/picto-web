package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;

public class PromiseViewCache {

  // map 'path' --> 'view cache'
  private final Map<String, PromiseView> promiseViewMap = new HashMap<String, PromiseView>();
  
  public void clear() {
    promiseViewMap.clear();
  }

  public void putPromiseView(PromiseView promiseView) {
    promiseViewMap.put(promiseView.getPath(), promiseView);
  }
  
  public void putPromiseView(String path, PromiseView promiseView) {
    promiseViewMap.put(path, promiseView);
  }

  public PromiseView getPromiseView(String path) {
    return promiseViewMap.get(path);
  }
}
