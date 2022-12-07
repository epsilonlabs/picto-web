package org.eclipse.epsilon.picto.web.component;

import org.eclipse.epsilon.picto.web.test.PerformanceTest.Client;

/***
 * Record for (always) generated vs cached view test
 * 
 * @author Alfa Yohannis
 *
 */
public class GeneratedVsCachedViewRecord {
  boolean alwaysGenerate;
  int iteration;
  Client client;
  String path;
  long duration;
  int payloadSize;

  public GeneratedVsCachedViewRecord(boolean alwaysGenerate, int iteration, Client client, String path, long duration,
      int payloadSize) {
    this.alwaysGenerate = alwaysGenerate;
    this.iteration = iteration;
    this.client = client;
    this.path = path;
    this.duration = duration;
    this.payloadSize = payloadSize;
  }

  public int getIteration() {
    return iteration;
  }

  public boolean isAlwaysGenerated() {
    return alwaysGenerate;
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