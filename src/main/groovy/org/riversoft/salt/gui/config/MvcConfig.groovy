package org.riversoft.salt.gui.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {

    @Value('${spring.mvc.locale}')
    String defaultLocaleString

    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor()
        localeChangeInterceptor.setParamName("lang")
        return localeChangeInterceptor
    }

    @Bean(name = "localeResolver")
    CookieLocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver()
        Locale defaultLocale = new Locale(defaultLocaleString)
        localeResolver.setDefaultLocale(defaultLocale)
        localeResolver.setCookieMaxAge(3600)
        localeResolver.setCookieName("locale")
        return localeResolver
    }

    @Bean
    LocaleResolver sessionLocaleResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver()
        Locale defaultLocale = new Locale(defaultLocaleString)
        localeResolver.setDefaultLocale(defaultLocale)
        return localeResolver
    }

    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

    @Bean
    RequestInterceptor requestInterceptor() {
        new RequestInterceptor()
    }

    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor())
        registry.addInterceptor(requestInterceptor())
    }
}
