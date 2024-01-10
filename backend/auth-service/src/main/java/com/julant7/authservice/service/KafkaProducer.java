package com.julant7.authservice.service;

public interface KafkaProducer {
    <T> void produce(String topic, T t);
}
