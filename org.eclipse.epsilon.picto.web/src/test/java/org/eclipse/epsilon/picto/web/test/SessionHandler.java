package org.eclipse.epsilon.picto.web.test;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/***
 * An internal handler class when connecting to the STOMP server.
 * 
 * @author Alfa Yohannis
 *
 */
class SessionHandler extends StompSessionHandlerAdapter {

  /***
   * Display the sessionId when connection to the Stomp Server is successful.
   */
  public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
    System.out.println("Connected with sessionId " + stompSession.getSessionId());
  }
}