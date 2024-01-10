package com.julant7.mailservice.service;

import com.julant7.mailservice.utils.KafkaMailMessage;
import com.julant7.mailservice.utils.MessageMode;

public interface MailService {
    void send(KafkaMailMessage kafkaMailMessage, MessageMode messageMode);
}
