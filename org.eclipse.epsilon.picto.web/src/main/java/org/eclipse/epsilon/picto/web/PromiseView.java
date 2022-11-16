package org.eclipse.epsilon.picto.web;

import java.sql.Timestamp;
import java.time.Instant;

import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.StaticContentPromise;
import org.eclipse.epsilon.picto.ViewContent;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PromiseView {

  private PictoView pictoView;
  private String timestamp = Timestamp.from(Instant.now()).toString();
  private boolean generate = true;
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

  public PromiseView(PictoView pictoView, ViewTree viewTree) {
    this.pictoView = pictoView;
    this.viewTree = viewTree;
    this.path = viewTree.getPathString();
  }

  public String getViewContent() throws Exception {
    return this.viewContent;
  }

  public ViewTree getViewTree() {
    return this.viewTree;
  }

  public String getViewContent(String clientTimestamp) throws Exception {

    if (path.equals(FileViewContentCache.PICTO_TREE)) {
      return viewContent;
    }

    ViewContent vc = null;
    if (generate) {

      if (viewTree.getPromise() instanceof StaticContentPromise) {

        pictoView.renderView(viewTree);
        vc = this.viewTree.getContent().getFinal(pictoView);
      } //
      else if (viewTree.getPromise() instanceof IncrementalLazyGenerationRuleContentPromise) {

        IncrementalLazyEgxModule module = (IncrementalLazyEgxModule) ((IncrementalLazyGenerationRuleContentPromise) viewTree
            .getPromise())
            .getGenerationRule().getModule();

        module.startRecording();
        pictoView.renderView(viewTree);
        vc = viewTree.getContent().getFinal(pictoView);
        module.stopRecording();

//        for (IModel model : module.getContext().getModelRepository().getModels()) {
//          model.close();
//        }
      }

      PictoResponse pictoResponse = new PictoResponse();
      pictoResponse.setFilename(filename);
      pictoResponse.setPath(this.path);
      pictoResponse.setTimestamp(this.timestamp);
      emptyViewContent = new ObjectMapper().writerWithDefaultPrettyPrinter()
          .writeValueAsString(pictoResponse);

      pictoResponse = new PictoResponse();
      pictoResponse.setFilename(filename);
      pictoResponse.setPath(this.path);
      pictoResponse.setTimestamp(this.timestamp);
      pictoResponse.setType(vc.getFormat());
      pictoResponse.setContent(vc.getText());
      viewContent = new ObjectMapper().writerWithDefaultPrettyPrinter()
          .writeValueAsString(pictoResponse);

      this.generate = false;
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

}
