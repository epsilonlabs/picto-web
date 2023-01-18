package org.eclipse.epsilon.picto.incrementality;

import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Module;
import org.eclipse.epsilon.picto.pictograph.Resource;
import org.eclipse.epsilon.picto.pictograph.Property;
import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class TraceIndex {

  protected Map<String, Path> pathIndex = new HashMap<String, Path>();
  protected Map<String, Module> moduleIndex = new HashMap<String, Module>();
  protected Map<String, Rule> ruleIndex = new HashMap<String, Rule>();
  protected Map<String, Template> templateIndex = new HashMap<String, Template>();
  protected Map<String, Resource> resourceIndex = new HashMap<String, Resource>();
  protected Map<String, Element> elementIndex = new HashMap<String, Element>();
  protected Map<String, Property> propertyIndex = new HashMap<String, Property>();
  protected List<Map<String, ?>> allIndices = new ArrayList<>(
      Arrays.asList(pathIndex, moduleIndex, ruleIndex, templateIndex, resourceIndex, elementIndex, propertyIndex));

  public int size() {
    int total = 0;
    for (int i = 0; i < allIndices.size(); i++) {
      total = total + allIndices.get(i).size();
    }
    return total;
  }

  public List<Map<String, ?>> getAllIndices() {
    return allIndices;
  }

  public Map<String, Path> getPathIndex() {
    return pathIndex;
  }

  public Path getPath(String pathName) {
    String key = pathName;
    return pathIndex.get(key);
  }

  public Path putPath(String pathName, Path path) {
    String key = pathName;
    return pathIndex.put(key, path);
  }

  /*
   * public Path getPath(Path path) { return null; }
   * 
   * public Path putPath(Path path) { return null; }
   */
  public Map<String, Module> getModuleIndex() {
    return moduleIndex;
  }

  public Module getModule(String moduleName) {
    String key = moduleName;
    return moduleIndex.get(key);
  }

  public Module putModule(String moduleName, Module module) {
    String key = moduleName;
    return moduleIndex.put(key, module);
  }

  /*
   * public Module getModule(Module module) { return null; }
   * 
   * public Module putModule(Module module) { return null; }
   */
  public Map<String, Rule> getRuleIndex() {
    return ruleIndex;
  }

  public Rule getRule(String moduleName, String rulesName) {
    String key = moduleName + "#" + rulesName;
    return ruleIndex.get(key);
  }

  public Rule putRule(String moduleName, String rulesName, Rule rule) {
    String key = moduleName + "#" + rulesName;
    return ruleIndex.put(key, rule);
  }

  /*
   * public Rule getRule(Module module, String rulesName) { return null; }
   * 
   * public Rule putRule(Module module, String rulesName) { return null; }
   */
  public Map<String, Template> getTemplateIndex() {
    return templateIndex;
  }

  public Template getTemplate(String moduleName, String rulesName, String templateName) {
    String key = moduleName + "#" + rulesName + "#" + templateName;
    return templateIndex.get(key);
  }

  public Template putTemplate(String moduleName, String rulesName, String templateName, Template template) {
    String key = moduleName + "#" + rulesName + "#" + templateName;
    return templateIndex.put(key, template);
  }

  /*
   * public Template getTemplate(Module module, String rulesName, String
   * templateName) { return null; }
   * 
   * public Template putTemplate(Module module, String rulesName, String
   * templateName) { return null; }
   */
  public Map<String, Resource> getResourceIndex() {
    return resourceIndex;
  }

  public Resource getResource(String resourceName) {
    String key = resourceName;
    return resourceIndex.get(key);
  }

  public Resource putResource(String resourceName, Resource resource) {
    String key = resourceName;
    return resourceIndex.put(key, resource);
  }

  /*
   * public Resource getResource(Resource resource) { return null; }
   * 
   * public Resource putResource(Resource resource) { return null; }
   */
  public Map<String, Element> getElementIndex() {
    return elementIndex;
  }

  public Element getElement(String resourceName, String elementsName) {
    String key = resourceName + "#" + elementsName;
    return elementIndex.get(key);
  }

  public Element putElement(String resourceName, String elementsName, Element element) {
    String key = resourceName + "#" + elementsName;
    return elementIndex.put(key, element);
  }

  /*
   * public Element getElement(Resource resource, String elementsName) { return
   * null; }
   * 
   * public Element putElement(Resource resource, String elementsName) { return
   * null; }
   */
  public Map<String, Property> getPropertyIndex() {
    return propertyIndex;
  }

  public Property getProperty(String resourceName, String elementsName, String propertiesName) {
    String key = resourceName + "#" + elementsName + "#" + propertiesName;
    return propertyIndex.get(key);
  }

  public Property putProperty(String resourceName, String elementsName, String propertiesName, Property property) {
    String key = resourceName + "#" + elementsName + "#" + propertiesName;
    return propertyIndex.put(key, property);
  }

  /*
   * public Property getProperty(Resource resource, String elementsName, String
   * propertiesName) { return null; }
   * 
   * public Property putProperty(Resource resource, String elementsName, String
   * propertiesName) { return null; }
   */
}
