package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IncrementalResource {

	void add(PropertyAccessRecord propertyAccessRecord);

	public List<PropertyAccessRecord> getIncrementalRecords();

	public void printIncrementalRecords();

	public List<PropertyAccessRecord> getModifiedPropertiesTargets();

	public void updateStatusToProcessed(String path);

	List<String> getNewPaths();

	List<PropertyAccessRecord> getNewAccessRecords();

	void updateStatusToProcessed(Collection<String> paths);

	Set<String> getToBeProcessedPaths();
}
