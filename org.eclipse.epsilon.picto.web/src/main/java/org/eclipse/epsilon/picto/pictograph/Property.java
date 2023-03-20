package org.eclipse.epsilon.picto.pictograph;

public class Property extends InputEntity {
  protected Element element;
  protected String previousValue;
  protected String value;

  public Element getElement() {
    return element;
  }

  public void setElement(Element element) {
    this.element = element;
    if (!element.getProperties().contains(this)) {
      element.getProperties().add(this);
    }
  }

  public String getPreviousValue() {
    return previousValue;
  }

  public void setPreviousValue(String previousValue) {
    this.previousValue = previousValue;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}