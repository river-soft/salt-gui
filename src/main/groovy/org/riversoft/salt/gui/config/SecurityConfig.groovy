package org.riversoft.salt.gui.config

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@CompileStatic
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()

        http.authorizeRequests().antMatchers('/login').permitAll().anyRequest().permitAll()

        http.formLogin().loginPage("/login").failureUrl("/login?error=1").successForwardUrl('/')

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/")

    }

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) {

        auth.inMemoryAuthentication().withUser("user").password("").roles("USER")
    }
}