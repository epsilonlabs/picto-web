/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.StaticContentPromise;
import org.eclipse.epsilon.picto.ViewContent;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.web.test.PerformanceRecord;
import org.eclipse.epsilon.picto.web.test.PerformanceRecorder;
import org.eclipse.epsilon.picto.web.test.PerformanceTestType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PromiseView {

//  private static final ExecutorService promiseExecutor = Executors.newFixedThreadPool(1);
//  private static final ExecutorService promiseExecutor = Executors.newSingleThreadExecutor();

  private PictoView pictoView;
  private String timestamp = Timestamp.from(Instant.now()).toString();
  private boolean hasBeenGenerated = false;
  private String path;
  private String filename;
  private ViewTree viewTree;
  private String viewContent; // this is the serialised picto response
  private String emptyViewContent;

  public PromiseView(PictoResponse pictoResponse) throws JsonProcessingException {
    this(pictoResponse.getPath(), pictoResponse);
  }

  public PromiseView(String path, PictoResponse pictoResponse) throws JsonProcessingException {
    pictoResponse.setTimestamp(this.timestamp);
    this.path = path;
    this.filename = pictoResponse.getFilename();
    this.viewContent = (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);
  }

  public PromiseView(String pictoFilePath, PictoView pictoView, ViewTree viewTree) {
    this.pictoView = pictoView;
    this.filename = pictoFilePath;
    this.viewTree = viewTree;
    this.path = viewTree.getPathString();
  }

  public String getViewContent() throws Exception {
    return this.viewContent;
  }

  public ViewTree getViewTree() {
    return this.viewTree;
  }

  @SuppressWarnings("null")
  public String getViewContent(String clientTimestamp) throws Exception {

    if (path.equals(FileViewContentCache.PICTO_TREE)) {
      return viewContent;
    }

//		System.out.print("Request " + this.path + " ... ");

    @Nonnull
    ViewContent vc = null;
    if (hasBeenGenerated == false || PictoApplication.isNoCache()) {

      if (viewTree.getPromise() instanceof StaticContentPromise) {

        pictoView.renderView(viewTree);
        vc = this.viewTree.getContent().getFinal(pictoView);

      } else if (viewTree.getPromise() instanceof IncrementalLazyGenerationRuleContentPromise) {

//        System.out.println("Generate " + path);
        IncrementalLazyEgxModule module = (IncrementalLazyEgxModule) ((IncrementalLazyGenerationRuleContentPromise) viewTree
            .getPromise()).getGenerationRule().getModule();

       
//        Future<ViewContent> result = promiseExecutor.submit(new GetViewContentTask(module, pictoView, viewTree));
//        vc = result.get();

//        module.startRecording();
        pictoView.renderView(viewTree);
        vc = viewTree.getContent().getFinal(pictoView);
        
//        module.stopRecording();

        

      }

      PictoResponse pictoResponse = new PictoResponse();
      pictoResponse.setFilename(filename);
      pictoResponse.setPath(this.path);
      pictoResponse.setTimestamp(this.timestamp);
      emptyViewContent = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);

      pictoResponse = new PictoResponse();
      pictoResponse.setFilename(filename);
      pictoResponse.setPath(this.path);
      pictoResponse.setTimestamp(this.timestamp);
      pictoResponse.setType(vc.getFormat());
      pictoResponse.setContent(vc.getText());
      viewContent = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);

      this.hasBeenGenerated = true;
//			System.out.println(path + " (re)generated");
    } else {
//			System.out.println(path + " cache returned");
    }

    if (clientTimestamp != null && timestamp.compareTo(clientTimestamp) <= 0) {
      return emptyViewContent;
    }

    return viewContent;
  }

  public void setViewContent(String viewContent) {
    this.viewContent = viewContent;
  }

  public String getPath() {
    return path;
  }

//  public static ExecutorService getPromiseExecutor() {
//    return promiseExecutor;
//  }

  public class GetViewContentTask implements Callable<ViewContent> {

    IncrementalLazyEgxModule module;
    PictoView pictoView;
    ViewTree viewTree;

    public GetViewContentTask(IncrementalLazyEgxModule module, PictoView pictoView, ViewTree viewTree) {
      this.module = module;
      this.pictoView = pictoView;
      this.viewTree = viewTree;
    }

    public ViewContent call() throws Exception {

//      module.startRecording();
      pictoView.renderView(viewTree);
      ViewContent vc = viewTree.getContent().getFinal(pictoView);
//      module.stopRecording();

      return vc;
    }

  }

  public boolean hasBeenGenerated() {
    return hasBeenGenerated;
  }

  public void setHasBeenGenerated(boolean hasBeenGenerated) {
    this.hasBeenGenerated = hasBeenGenerated;
  }

  public String getEmptyViewContent() {
    return emptyViewContent;
  }

  public void setEmptyViewContent(String emptyViewContent) {
    this.emptyViewContent = emptyViewContent;
  }

}
