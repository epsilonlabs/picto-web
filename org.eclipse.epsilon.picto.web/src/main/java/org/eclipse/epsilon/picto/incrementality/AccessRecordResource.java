package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;

public interface AccessRecordResource {

	void add(GenerationRulePropertyAccess propertyAccessRecord);

	public List<GenerationRulePropertyAccess> getIncrementalRecords();

	public void printIncrementalRecords();

	public void updateStatusToProcessed(String path);

	void updateStatusToProcessed(Collection<String> paths);

	Set<String> getToBeProcessedPaths(List<LazyGenerationRuleContentPromise> inProcessingPromises, EgxModule module);

	void clear();

	void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId, String path);


}
