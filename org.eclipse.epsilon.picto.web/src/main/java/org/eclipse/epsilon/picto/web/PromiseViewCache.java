package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Map;


public class PromiseViewCache {

  // map 'path' --> 'view cache'
  private final Map<String, PromiseView> promiseViewMap = new HashMap<String, PromiseView>();

  public Map<String, PromiseView> getMap() {
    return promiseViewMap;
  }

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
