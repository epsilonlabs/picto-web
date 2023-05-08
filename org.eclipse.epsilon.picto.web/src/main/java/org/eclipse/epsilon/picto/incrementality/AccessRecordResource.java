package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.Path;

public interface AccessRecordResource {

	public Set<String> getDeletedPaths();
		
	public void add(AccessRecord access);

	public List<AccessRecord> getIncrementalRecords();

	public void printIncrementalRecords();

	public void updateStatusToProcessed(Collection<String> paths);

	public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
			EgxModule module);

	public void clear();
	
	public Map<String, Path> getPaths();

	public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
			String path);

	public boolean getPathStatus(String pathString);

	public int size();

	public void addAll(List<AccessRecord> currentPropertyAccesses);

}
