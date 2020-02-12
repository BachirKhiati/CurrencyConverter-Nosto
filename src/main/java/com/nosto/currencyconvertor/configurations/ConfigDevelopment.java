package com.nosto.currencyconvertor.configurations;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// -Dspring.profiles.active=dev
@Configuration
@Profile("dev")
class ConfigDevelopment extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().anyRequest().requiresInsecure().and()
                .authorizeRequests()
                .antMatchers("/**/*.{js,html,css}").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .and()
                .csrf().disable();
    }
}
