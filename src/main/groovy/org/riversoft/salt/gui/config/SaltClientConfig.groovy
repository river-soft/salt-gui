package org.riversoft.salt.gui.config

import org.riversoft.salt.gui.client.SaltClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SaltClientConfig {

    @Value('${salt.api.url}')
    private String SALT_API_URL

    @Bean
    SaltClient saltClient() {
        return new SaltClient(URI.create(SALT_API_URL));
    }

}
