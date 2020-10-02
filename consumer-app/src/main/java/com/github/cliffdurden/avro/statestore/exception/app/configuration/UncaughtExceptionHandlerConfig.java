package com.github.cliffdurden.avro.statestore.exception.app.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;

@Slf4j
@Configuration
public class UncaughtExceptionHandlerConfig {

    @Bean
    StreamsBuilderFactoryBeanCustomizer customizer() {
        return factoryBean ->
                factoryBean.setUncaughtExceptionHandler(
                        (thread, throwable) -> {
                            log.error("UncaughtException", throwable);
                            factoryBean.stop();
                            factoryBean.start();
                            // FIXE: or System.exit(1);
                        }
                );
    }
}
