package org.riversoft.salt.gui.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    RequestInterceptor requestInterceptor() {
        new RequestInterceptor()
    }

    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor())
    }
}
