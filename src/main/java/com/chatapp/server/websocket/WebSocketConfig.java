package com.chatapp.server.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.chatapp.server.dto.MessageRequest;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry)
	{
		registry.addEndpoint("/ws").setAllowedOrigins("*");
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry)
	{
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
		
	    registry.setUserDestinationPrefix("/user");
	    registry.setPathMatcher(new AntPathMatcher());
	}
	
	@Bean
	public ApplicationListener<SessionSubscribeEvent> subscribeEvent() 
	{
	    return event -> {
	        System.out.println("SUBSCRIBE EVENT: " + event.getMessage());
	    };
	}
	
	@Bean
	public ApplicationListener<SessionUnsubscribeEvent> unsubscribeListener() {
	    return event -> {
	        System.out.println("UNSUBSCRIBE EVENT: " + event);
	    };
	}
}
