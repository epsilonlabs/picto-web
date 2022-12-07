package org.eclipse.epsilon.picto.web.component;

import org.eclipse.epsilon.picto.web.test.PerformanceTest.Client;

/***
 * Record for All vs Selective Regeneration Test
 * 
 * @author Alfa Yohannis
 *
 */
public class GreedyVsSelectiveGenerationRecord {

  boolean genAll;
  int numOfAffectedView;
  int iteration;
  Client client;
  String path;
  long duration;
  int payloadSize;

  public GreedyVsSelectiveGenerationRecord(boolean genAll, int numOfAffectedView, int iteration, Client client,
      String path,
      long duration,
      int payloadSize) {
    this.genAll = genAll;
    this.numOfAffectedView = numOfAffectedView;
    this.iteration = iteration;
    this.client = client;
    this.path = path;
    this.duration = duration;
    this.payloadSize = payloadSize;
  }

  public boolean isGenAll() {
    return genAll;
  }

  public int getNumOfAffectedView() {
    return numOfAffectedView;
  }

  public int getIteration() {
    return iteration;
  }

  public Client getClient() {
    return client;
  }

  public String getPath() {
    return path;
  }

  public long getDuration() {
    return duration;
  }

  public int getPayloadSize() {
    return payloadSize;
  }

}