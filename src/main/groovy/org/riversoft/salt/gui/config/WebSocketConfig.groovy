package org.riversoft.salt.gui.config

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@Slf4j('logger')
@Configuration
@EnableWebSocketMessageBroker
//@EnableScheduling
class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    void registerStompEndpoints(StompEndpointRegistry registry) {

        logger.debug('Configure custom StompEndpoints')

        registry.addEndpoint("/salt").setAllowedOrigins('*').withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        logger.debug('Configure custom MessageBroker')
        registry.enableSimpleBroker('/response', '/queue')
        registry.setApplicationDestinationPrefixes('/request')
    }
}