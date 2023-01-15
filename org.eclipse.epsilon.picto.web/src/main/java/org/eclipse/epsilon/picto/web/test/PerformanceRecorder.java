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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Alfa Yohannis
 *
 */
public class PerformanceRecorder {

  private static final ExecutorService RECORDING_EXECUTOR = Executors.newSingleThreadExecutor();

  private static boolean isRecording = false;
  public static long fileChangeTime = 0;
  private static List<PerformanceRecord> performanceRecords = Collections.synchronizedList(new ArrayList<>());

  public static boolean generateAlways;
  public static boolean genenerateAll;
  public static int globalNumberIteration;
  public static int globalActualAffectedViews;
  public static int globalNumberOfAffectedViews;
  public static long startTime;
  public static long detectionTime;
  public static long loadingTime;
  public static int globalNumberOfViews;

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

  public static void saveRecord(File outputFile, PerformanceRecord record)
      throws InterruptedException, ExecutionException {
    String line = ((record.isGenAll()) ? "all" : "N") + "," + record.getNumOfInvalidatedViews() + ","
        + record.getIteration() + "," + record.getClient() + "," + record.getPath() + "," + record.getDuration() + ","
        + record.getPayloadSize() + "," + record.getType() + System.lineSeparator();
    PerformanceRecorder.saveRecord(outputFile, line);
  }

  public static void saveRecord(File outputFile, String text) throws InterruptedException, ExecutionException {
    Future<Path> result = RECORDING_EXECUTOR.submit(new RecordingTask(outputFile, text));
    result.get();
  }

  public static class RecordingTask implements Callable<Path> {

    String text;
    File outputFile;

    public RecordingTask(File outputFile, String text) {
      this.text = text;
      this.outputFile = outputFile;
    }

    public Path call() throws Exception {
      return Files.write(Path.of(outputFile.toURI()), (text + System.lineSeparator()).getBytes(),
          StandardOpenOption.APPEND);
    }

  }

}
