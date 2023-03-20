package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public abstract class InputEntity extends Entity {
  protected List<Path> affects = new LinkedList<>();

  public List<Path> getAffects() {
    return affects;
  }

  public void addAffectedPath(Path path) {
    if (!this.affects.contains(path)) {
      this.affects.add(path);
    }
    if (!path.getAffectedBy().contains(this)) {
      path.getAffectedBy().add(this);
    }
  }

  public void setAffects(List<Path> affects) {
    this.affects = affects;
  }
}