package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.State;

public interface AccessRecordResource {

  public void add(AccessRecord access);

  public List<AccessRecord> getIncrementalRecords();

  public void printIncrementalRecords();

  public void updateStatusToProcessed(Collection<String> paths);

  public Set<String> getInvalidatedViewPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
      EgxModule module);

  public void clear();

  public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
      String path);

  public State getPathStatus(String pathString);

  public int size();

}
