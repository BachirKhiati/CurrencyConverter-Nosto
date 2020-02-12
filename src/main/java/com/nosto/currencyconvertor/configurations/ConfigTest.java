package com.nosto.currencyconvertor.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


// -Dspring.profiles.active=test
@Configuration
@Profile("test")
class ConfigTest extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().sameOrigin().and().
                authorizeRequests().anyRequest().anonymous().and().httpBasic().disable();
    }

}

