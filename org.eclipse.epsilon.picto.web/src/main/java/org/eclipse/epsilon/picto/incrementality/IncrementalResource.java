package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;

public interface IncrementalResource {

	void add(PropertyAccessRecord propertyAccessRecord);

	public List<PropertyAccessRecord> getIncrementalRecords();

	public void printIncrementalRecords();

	public List<PropertyAccessRecord> getGeneratedPropertyAccessRecords();

	public void updateStatusToProcessed(String path);

//	List<String> getNewPaths();

//	List<PropertyAccessRecord> getNewAccessRecords();

	void updateStatusToProcessed(Collection<String> paths);

	Set<String> getToBeProcessedPaths(List<LazyGenerationRuleContentPromise> inProcessingPromises, EgxModule module);

	void clear();


}
