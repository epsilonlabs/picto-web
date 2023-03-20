package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Resource extends InputEntity {

  protected List<Element> elements = new LinkedList<>();

  public void addElement(Element element) {
    if (!elements.contains(element)) {
      elements.add(element);
    }
    element.setResource(this);
  }

  public List<Element> getElements() {
    return elements;
  }
}
