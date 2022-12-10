/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web.test;

/***
 * Record for All vs Selective Regeneration Test
 * 
 * @author Alfa Yohannis
 *
 */
public class PerformanceRecord {

  boolean genAll;
  boolean alwaysGenerate;
  int numOfInvalidatedViews;
  int iteration;
  String client;
  String path;
  long duration;
  int payloadSize;
  String type;

  public PerformanceRecord(boolean genAll, boolean alwaysGenerate, int numOfInvalidatedViews, int iteration, String client,
      String path,
      long duration,
      int payloadSize, String type) {
    this.genAll = genAll;
    this.numOfInvalidatedViews = numOfInvalidatedViews;
    this.iteration = iteration;
    this.client = client;
    this.path = path;
    this.duration = duration;
    this.payloadSize = payloadSize;
    this.type = type;
  }

  public boolean isGenAll() {
    return genAll;
  }

  public int getNumOfInvalidatedViews() {
    return numOfInvalidatedViews;
  }

  public int getIteration() {
    return iteration;
  }

  public String getClient() {
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

  /**
   * @return
   */
  public boolean isAlwaysGenerated() {
    return false;
  }

  public String getType() {
    return type;
  }

}