package org.eclipse.epsilon.picto.web;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeEventListener implements ApplicationListener<ApplicationEvent> {

  public static int counter = 0;

  public void onApplicationEvent(ApplicationEvent event) {
//    System.out.println(++counter + ". " + event.getClass().getName());
    if (event instanceof SessionSubscribeEvent) {
//      SessionSubscribeEvent e = (SessionSubscribeEvent) event;
//      StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(e.getMessage());
//      System.out.println(headerAccessor.getSessionAttributes().get("sessionId").toString());
    } else if (event instanceof SessionDisconnectEvent) {
//      SessionDisconnectEvent e = (SessionDisconnectEvent) event;
//      System.console();
    }

  }
}