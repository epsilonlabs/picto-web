package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Rule extends InputEntity {

  protected Module module;
  protected Template template;
  protected List<Element> contextElements = new LinkedList<>();
  protected List<Element> elements = new LinkedList<>();

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
    if (!module.getRules().contains(this)) {
      module.getRules().add(this);
    }
  }

  public Template getTemplate() {
    return template;
  }

  public void setTemplate(Template template) {
    this.template = template;
    if (!template.getRules().contains(this)) {
      template.getRules().add(this);
    }
  }

  public List<Element> getContextElements() {
    return contextElements;
  }

  public void addContextElement(Element element) {
    if (!this.contextElements.contains(element)) {
      this.contextElements.add(element);
    }
  }

  public void addElement(Element element) {
    if (!this.elements.contains(element)) {
      this.elements.add(element);
    }
  }

  public List<Element> getElements() {
    return elements;
  }
}