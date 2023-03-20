package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Element extends InputEntity {
  protected Resource resource;
  protected List<Property> properties = new LinkedList<>();

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
    if (!this.resource.getElements().contains(this)) {
      this.resource.getElements().add(this);
    }
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void addProperty(Property property) {
    if (!this.properties.contains(property)) {
      this.properties.add(property);
    }
    property.setElement(this);
  }

  public void removeProperty(Property property) {
    if (!this.properties.contains(property)) {
      this.properties.remove(property);
    }
    property.setElement(null);
  }

}