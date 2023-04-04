package org.eclipse.epsilon.picto.pictograph;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Path {

  protected boolean isNew = true;
  protected String name;
  protected ConcurrentHashMap<String, Property> properties = new ConcurrentHashMap<>();
//  protected Map<String, Property> properties = new HashMap();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

//  public ConcurrentHashMap<String, Property> getProperties() {
  public Map<String, Property> getProperties() {
    return properties;
  }

  public void setProperties(ConcurrentHashMap<String, Property> properties) {
    this.properties = properties;
  }

  public Property putProperty(String propertyId, Property property) {
    return this.properties.put(propertyId, property);
  }

  public Property getProperty(String propertyId) {
    return this.properties.get(propertyId);
  }

  public boolean isNew() {
    return isNew;
  }

  public void setOld() {
    this.isNew = false;
  }
}