package org.eclipse.epsilon.picto.web;

import java.sql.Timestamp;
import java.time.Instant;

public class PictoResponse {

  private String hash;
  private String timestamp = Timestamp.from(Instant.now()).toString();
  private String filename;
  private String path;
  private String type;
  private String content;

  public PictoResponse() {
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public PictoResponse(String content) {
    this.content = content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

}