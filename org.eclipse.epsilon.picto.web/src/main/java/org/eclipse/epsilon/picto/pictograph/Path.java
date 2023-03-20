package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Path extends Entity {
  protected List<InputEntity> affectedBy = new LinkedList<>();
  protected int generationCount;
  protected long generationTime;
  double avgGenTime;
  int checkCount;
  protected long checkingTime;
  protected double avgCheckTime;

  public void addAffectedBy(InputEntity affectedBy) {
    if (!this.affectedBy.contains(affectedBy)) {
      this.affectedBy.add(affectedBy);
    }
    if (!affectedBy.getAffects().contains(this)) {
      affectedBy.getAffects().add(this);
    }
  }

  public void setAffectedBy(List<InputEntity> affectedBy) {
    this.affectedBy = affectedBy;
  }

  public void setGenerationCount(int generationCount) {
    this.generationCount = generationCount;
  }

  public void setGenerationTime(long generationTime) {
    this.generationTime = generationTime;
  }

  public void setAvgGenTime(double avgGenTime) {
    this.avgGenTime = avgGenTime;
  }

  public void setCheckCount(int checkCount) {
    this.checkCount = checkCount;
  }

  public void setCheckingTime(long checkingTime) {
    this.checkingTime = checkingTime;
  }

  public void setAvgCheckTime(double avgCheckTime) {
    this.avgCheckTime = avgCheckTime;
  }

  public List<InputEntity> getAffectedBy() {
    return affectedBy;
  }

  public int getGenerationCount() {
    return generationCount;
  }

  public long getGenerationTime() {
    return generationTime;
  }

  public double getAvgGenTime() {
    return avgGenTime;
  }

  public int getCheckCount() {
    return checkCount;
  }

  public long getCheckingTime() {
    return checkingTime;
  }

  public double getAvgCheckTime() {
    return avgCheckTime;
  }

}