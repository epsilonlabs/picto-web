package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.pictograph.State;

public interface AccessRecordResource {

  void add(AccessRecord propertyAccessRecord);

  public List<AccessRecord> getIncrementalRecords();

  public void printIncrementalRecords();

  public void updateStatusToProcessed(Collection<String> paths);

  public Set<String> getToBeProcessedPaths(List<IncrementalLazyGenerationRuleContentPromise> promises,
      EgxModule module);

  public void clear();

  public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
      String path);

  State getPathStatus(String pathString);



}
