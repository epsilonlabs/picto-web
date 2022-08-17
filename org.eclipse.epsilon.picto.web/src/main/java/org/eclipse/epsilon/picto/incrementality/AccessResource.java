package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;

public interface AccessResource {

	void add(Access propertyAccessRecord);

	public List<Access> getIncrementalRecords();

	public void printIncrementalRecords();

//	public void updateStatusToProcessed(String path);

	public void updateStatusToProcessed(Collection<String> paths);

	public Set<String> getToBeProcessedPaths(List<LazyGenerationRuleContentPromise> inProcessingPromises, EgxModule module);

	public void clear();

	public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId, String path);


}
