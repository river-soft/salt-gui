package org.riversoft.salt.gui.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
//                .simpSubscribeDestMatchers("/salt/queue/errors").permitAll()
//                .simpDestMatchers("/").authenticated()
//                .simpDestMatchers("/job-results-counts").hasRole("ROOT")
//                .simpSubscribeDestMatchers("/user/**", "/topic/**").hasRole("USER")
//                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
//                .anyMessage().denyAll()
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true
    }
}
