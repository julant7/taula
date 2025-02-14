package com.julant7.userservice.service;

public interface KafkaProducer {
    <T> void produce(String topic, T t);
}
