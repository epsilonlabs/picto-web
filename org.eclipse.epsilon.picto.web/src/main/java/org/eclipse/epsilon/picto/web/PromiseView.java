package org.eclipse.epsilon.picto.web;

import org.eclipse.epsilon.picto.ContentPromise;
import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.StaticContentPromise;
import org.eclipse.epsilon.picto.ViewContent;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.incrementality.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PromiseView {

  private PictoView pictoView;
  private String path;
  private String filename;
  private String viewHash = "";
  private ContentPromise promise;
  private ViewTree viewTree;
  private String viewContent; // this is the serialised picto response

  public PromiseView(PictoResponse pictoResponse) throws JsonProcessingException {
    this(pictoResponse.getPath(), pictoResponse);
  }

  public PromiseView(String path, PictoResponse pictoResponse) throws JsonProcessingException {
    this.path = path;
    this.viewHash = pictoResponse.getHash();
    this.filename = pictoResponse.getFilename();
    this.viewContent = (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);
    this.viewHash = Util.getHash(pictoResponse.getContent());
  }

  public PromiseView(PictoView pictoView, ViewTree viewTree) {
    this.pictoView = pictoView;
    this.viewTree = viewTree;
    this.path = viewTree.getPathString();
    this.promise = viewTree.getPromise();
    this.viewHash = Util.getHash(viewTree.getContent().getText());

  }

  public String getViewHash() {
    return viewHash;
  }

  public String getViewContent() throws Exception {
    return this.viewContent;
  }

  public String getViewContent(String requestedHash) throws Exception {
    if (viewContent == null || !viewHash.equals(requestedHash)) {

      ViewContent vc = null;
      
      if (path.equals(FileViewContentCache.PICTO_TREE)) {
        return viewContent;
      } //
      else if (promise instanceof StaticContentPromise) {

        pictoView.renderView(viewTree);
        vc = this.viewTree.getContent().getFinal(pictoView);
      } //
      else if (promise instanceof IncrementalLazyGenerationRuleContentPromise) {

        IncrementalLazyEgxModule module = (IncrementalLazyEgxModule) ((IncrementalLazyGenerationRuleContentPromise) promise)
            .getGenerationRule().getModule();

        module.startRecording();
        pictoView.renderView(viewTree);
        vc = viewTree.getContent().getFinal(pictoView);
        module.stopRecording();
      }

      PictoResponse pictoResponse = new PictoResponse();
      pictoResponse.setHash(this.viewHash);
      pictoResponse.setFilename(filename);
      pictoResponse.setPath(this.path);
      pictoResponse.setType(vc.getFormat());
      pictoResponse.setContent(vc.getText());
      viewContent = new ObjectMapper().writerWithDefaultPrettyPrinter()
          .writeValueAsString(pictoResponse);
    }

    return viewContent;
  }

  public void setViewContent(String viewContent) {
    this.viewContent = viewContent;
  }

  public String getPath() {
    return path;
  }

}
