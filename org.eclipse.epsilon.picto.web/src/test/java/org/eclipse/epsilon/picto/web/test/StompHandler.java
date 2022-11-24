package org.eclipse.epsilon.picto.web.test;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

/***
 * An internal handler class that handles the STOMP response from the server.
 * Using this class, we can get the values, the returned pages, returned from
 * Picto Web server.
 * 
 * @author Alfa Yohannis
 *
 */
class StompHandler implements StompFrameHandler {
  private ResponseHolder answer;

  /***
   * The constructor receives a CompletableFuture<String> to hold the value (json
   * object string) returned from the server.
   * 
   * @param answer
   * @param numberOfExpectedJsons
   */
  StompHandler(ResponseHolder answer) {
    this.answer = answer;
  }

  public Type getPayloadType(StompHeaders headers) {
    return byte[].class;
  }

  /***
   * This method receives the page returned by Picto Web server.
   * 
   * @param payload The payload (.e.g., json, html) returned by the server.
   * @param headers The Stomp Headers.
   */
  public void handleFrame(StompHeaders headers, Object payload) {
    answer.getResponseStrings().add((new String((byte[]) payload)));
  }
}