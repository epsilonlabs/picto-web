/*********************************************************************
* Copyright (c) 2023 The University of York.
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.epsilon.picto.incrementality.AccessGraphResource;
import org.eclipse.epsilon.picto.incrementality.AccessRecordResource;

/**
 * @author Alfa Yohannis
 *
 */
public class PerformanceRecorder {

  private static final ExecutorService RECORDING_EXECUTOR = Executors.newSingleThreadExecutor();

  private static boolean isRecording = false;
  public static long fileChangeTime = 0;

  public static boolean generateAlways;
  public static boolean genenerateAll;
  public static int globalNumberIteration;
  public static int globalActualAffectedViews;
  public static int globalNumberOfAffectedViews;
  public static long startTime;
  public static long initialisationTime;
  public static long detectionTime;
  public static long loadingTime;
  public static int globalNumberOfViews;
  public static int numberOfAffectingEntities;
  public static long generationTime;
  public static long promiseTime;
  public static File outputFile;
  public static AccessRecordResource accessRecordResource;
  public static int num;

  /***
   * The class that is responsible to write string to the output log file.
   * 
   * @author Alfa Yohannis
   *
   */
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

  /**
   * Save record to file.
   * 
   * @param record
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static void record(PerformanceRecord record) {
    try {
      if (isRecording) {
        String line = (++num) + "," + ((record.isGenAll()) ? "all" : "N") + "," + record.getNumOfInvalidatedViews() + ","
            + record.getIteration() + "," + record.getClient() + "," + record.getPath() + "," + record.getDuration()
            + "," + record.getPayloadSize() + "," + record.getType() + "," + PerformanceRecorder.getPropertyCount();
        PerformanceRecorder.record(line);
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * Save record to the file.
   * 
   * @param text
   * @throws InterruptedException
   * @throws ExecutionException
   */
  static void record(String text) throws InterruptedException, ExecutionException {
    if (isRecording) {
      Future<Path> result = RECORDING_EXECUTOR.submit(new RecordingTask(outputFile, text));
      result.get();
    }
  }

  /**
   * Add header to the file.
   * 
   * @param text
   * @throws InterruptedException
   * @throws ExecutionException
   */
  static void header(String text) throws InterruptedException, ExecutionException {
    record(text);
  }

  public static void initFile(File outputFile) throws IOException, InterruptedException, ExecutionException {
    if (!outputFile.getParentFile().exists())
      outputFile.getParentFile().mkdir();
    if (outputFile.exists())
      outputFile.delete();
    outputFile.createNewFile();
    PerformanceRecorder.outputFile = outputFile;
  }
  
  /**
   * Set the output file.
   * 
   * @param outputFile
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static void setOutputFile(File outputFile) throws IOException, InterruptedException, ExecutionException {
    PerformanceRecorder.outputFile = outputFile;
  }

  public static void startRecording() {
    isRecording = true;
  }

  public static void stopRecording() {
    isRecording = false;
  }

  public static boolean isRecording() {
    return isRecording;
  }

  public static long getFileChangeTime() {
    return fileChangeTime;
  }

  public static void setFileChangeTime(long fileChangeTime) {
    PerformanceRecorder.fileChangeTime = fileChangeTime;
  }

  public static boolean isGenerateAlways() {
    return generateAlways;
  }

  public static void setGenerateAlways(boolean generateAlways) {
    PerformanceRecorder.generateAlways = generateAlways;
  }

  public static boolean isGenenerateAll() {
    return genenerateAll;
  }

  public static void setGenenerateAll(boolean genenerateAll) {
    PerformanceRecorder.genenerateAll = genenerateAll;
  }

  public static int getGlobalNumberIteration() {
    return globalNumberIteration;
  }

  public static void setGlobalNumberIteration(int globalNumberIteration) {
    PerformanceRecorder.globalNumberIteration = globalNumberIteration;
  }

  public static int getGlobalActualAffectedViews() {
    return globalActualAffectedViews;
  }

  public static void setGlobalActualAffectedViews(int globalActualAffectedViews) {
    PerformanceRecorder.globalActualAffectedViews = globalActualAffectedViews;
  }

  public static int getGlobalNumberOfAffectedViews() {
    return globalNumberOfAffectedViews;
  }

  public static void setGlobalNumberOfAffectedViews(int globalNumberOfAffectedViews) {
    PerformanceRecorder.globalNumberOfAffectedViews = globalNumberOfAffectedViews;
  }

  public static long getStartTime() {
    return startTime;
  }

  public static void setStartTime(long startTime) {
    PerformanceRecorder.startTime = startTime;
  }

  public static long getDetectionTime() {
    return detectionTime;
  }

  public static void setDetectionTime(long detectionTime) {
    PerformanceRecorder.detectionTime = detectionTime;
  }

  public static long getLoadingTime() {
    return loadingTime;
  }

  public static void setLoadingTime(long loadingTime) {
    PerformanceRecorder.loadingTime = loadingTime;
  }

  public static int getGlobalNumberOfViews() {
    return globalNumberOfViews;
  }

  public static void setGlobalNumberOfViews(int globalNumberOfViews) {
    PerformanceRecorder.globalNumberOfViews = globalNumberOfViews;
  }

  public static int getNumberOfAffectingEntities() {
    return numberOfAffectingEntities;
  }

  public static void setNumberOfAffectingEntities(int numberOfAffectingEntities) {
    PerformanceRecorder.numberOfAffectingEntities = numberOfAffectingEntities;
  }

  public static long getGenerationTime() {
    return generationTime;
  }

  public static void setGenerationTime(long generationTime) {
    PerformanceRecorder.generationTime = generationTime;
  }

  public static long getPromiseTime() {
    return promiseTime;
  }

  public static void setPromiseTime(long promiseTime) {
    PerformanceRecorder.promiseTime = promiseTime;
  }

  public static File getOutputFile() {
    return outputFile;
  }

  public static void setRecording(boolean isRecording) {
    PerformanceRecorder.isRecording = isRecording;
  }

  public static int accessRecordResourceSize() {
    return accessRecordResource.size();
  }

  public static int getPropertyCount() {
    return ((AccessGraphResource) accessRecordResource).getTraceIndex().getPropertyIndex().size();
  }
}