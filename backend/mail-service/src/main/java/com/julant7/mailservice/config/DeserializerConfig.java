package com.julant7.mailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
public class DeserializerConfig {

    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }

}