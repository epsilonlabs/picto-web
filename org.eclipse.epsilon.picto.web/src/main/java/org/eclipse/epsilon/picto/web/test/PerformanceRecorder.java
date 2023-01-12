/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

/**
 * 
 */

package org.eclipse.epsilon.picto.web.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alfa Yohannis
 *
 */
public class PerformanceRecorder {

  private static boolean isRecording = false;
  public static long fileChangeTime = 0;
  private static List<PerformanceRecord> performanceRecords = Collections.synchronizedList(new ArrayList<>());

  public static void record(PerformanceRecord record) {
    if (isRecording)
      performanceRecords.add(record);
  }

  public static void startRecording() {
    isRecording = true;
  }

  public static void stopRecording() {
    isRecording = false;
    performanceRecords.clear();
  }

  public static boolean isRecording() {
    return isRecording;
  }

  public static List<PerformanceRecord> getPerformanceRecords() {
    return performanceRecords;
  }

  public static boolean generateAlways;
  public static boolean genenerateAll;
  public static int globalNumberIteration;
  public static int globalActualAffectedViews;
  public static int globalNumberOfAffectedViews;
  public static long startTime;
  public static long detectionTime;
  public static long loadingTime;
  public static int globalNumberOfViews;

}
