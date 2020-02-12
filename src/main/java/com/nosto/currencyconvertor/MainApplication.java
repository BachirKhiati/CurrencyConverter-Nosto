package com.nosto.currencyconvertor;

import io.github.sercasti.tracing.config.TracingConfig;
import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.filter.TracingFilter;
import io.github.sercasti.tracing.interceptor.TracingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainApplication {
    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public MainApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    protected Tracing tracing() {
        return TracingConfig.createTracing();
    }

    @Bean
    protected TracingFilter tracingFilter() {
        return new TracingFilter();
    }

    @Bean
    protected TracingInterceptor tracingInterceptor() {
        return new TracingInterceptor(tracing());
    }
}
