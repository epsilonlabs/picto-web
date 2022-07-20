package org.eclipse.epsilon.picto.incrementality;

import java.util.List;

public interface IncrementalResource {

	void add(PropertyAccessRecord propertyAccessRecord);

	public List<PropertyAccessRecord> getIncrementalRecords();
	
	public void printIncrementalRecords();
	
	public List<PropertyAccessRecord> getModifiedPropertiesTargets();
}
