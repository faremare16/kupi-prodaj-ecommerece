package com.faruk.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        // prefiks rute na koji se klijenti pretplacuju
        config.enableSimpleBroker("/topic");
        // prefiks rute sa koje klijent salje poruke server-u
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // websockeet endpoint na kojeg se angular kaci
        registry.addEndpoint("/api/v1/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
