package org.eclipse.epsilon.picto.web;

public class PictoResponse {

//  private String hash;
  private String timestamp;
  private String filename;
  private String path;
  private String type;
  private String content;

  public PictoResponse() {
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
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

//  public String getHash() {
//    return hash;
//  }
//
//  public void setHash(String hash) {
//    this.hash = hash;
//  }

}