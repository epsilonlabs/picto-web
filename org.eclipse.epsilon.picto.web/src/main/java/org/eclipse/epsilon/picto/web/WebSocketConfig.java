package org.eclipse.epsilon.picto.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.setSendTimeLimit(Integer.MAX_VALUE)
        .setSendBufferSizeLimit(Integer.MAX_VALUE)
        .setMessageSizeLimit(Integer.MAX_VALUE)
        .setTimeToFirstMessage(Integer.MAX_VALUE);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/picto-web").withSockJS()
        .setStreamBytesLimit(Integer.MAX_VALUE)
        .setHttpMessageCacheSize(Integer.MAX_VALUE)
        .setWebSocketEnabled(true)
        .setHeartbeatTime(Integer.MAX_VALUE)
        .setDisconnectDelay(Integer.MAX_VALUE);
  }

}