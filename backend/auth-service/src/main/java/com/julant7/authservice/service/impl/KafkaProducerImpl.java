package com.julant7.authservice.service.impl;

import com.julant7.authservice.service.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerImpl implements KafkaProducer {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KafkaProducerImpl.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T> void produce(String topic, T t) {
        kafkaTemplate.send(
            topic, t
        ).whenComplete((res, th) -> {
            log.info("produced message " + res.getProducerRecord() + " topic: " + res.getProducerRecord().topic());
        });
    }
}
