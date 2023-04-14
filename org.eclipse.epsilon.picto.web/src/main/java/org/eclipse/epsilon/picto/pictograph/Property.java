package org.eclipse.epsilon.picto.pictograph;

public class Property {
  protected boolean isDeleted = false;
  protected boolean isNew = true;
  protected String propertyId;
  protected String propertyName;
  protected String element;
  protected String resource;
  protected String value;
  protected String egxModule;
  protected String eglTemplate;
  protected String generationRule;

  public Property(String propertyId, String resource, String element, String propertyName, String value) {
    super();
    this.isNew = true;
    this.propertyId = propertyId;
    this.propertyName = propertyName;
    this.element = element;
    this.resource = resource;
    this.value = value;
  }

  public String toString() {
    return propertyId + ", " + value + ", deleted:" + isDeleted;
  }

  public void setDeleted() {
    this.isDeleted = true;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getElement() {
    return element;
  }

  public void setElement(String element) {
    this.element = element;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setOld() {
    isNew = false;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getEgxModule() {
    return egxModule;
  }

  public void setEgxModule(String egxModule) {
    this.egxModule = egxModule;
  }

  public String getEglTemplate() {
    return eglTemplate;
  }

  public void setEglTemplate(String eglTemplate) {
    this.eglTemplate = eglTemplate;
  }

  public String getGenerationRule() {
    return generationRule;
  }

  public void setGenerationRule(String generationRule) {
    this.generationRule = generationRule;
  }
}