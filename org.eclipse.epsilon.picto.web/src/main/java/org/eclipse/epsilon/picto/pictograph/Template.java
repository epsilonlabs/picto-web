package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Template extends InputEntity {

  protected List<Module> modules = new LinkedList<>();
  protected List<Rule> rules = new LinkedList<>();
  protected List<Element> elements = new LinkedList<>();

  public List<Module> getModules() {
    return modules;
  }

  public void addModule(Module module) {
    if (!this.modules.contains(module)) {
      this.modules.add(module);
    }
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void addRule(Rule rule) {
    if (!this.rules.contains(rule)) {
      this.rules.add(rule);
    }
  }

  public List<Element> getElements() {
    return elements;
  }

  public void addelement(Element element) {
    if (!this.elements.contains(element)) {
      this.elements.add(element);
    }
  }

}