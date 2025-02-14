package com.julant7.mailservice.listener;

import com.julant7.mailservice.service.MailService;
import com.julant7.mailservice.utils.KafkaMailMessage;
import com.julant7.mailservice.utils.MessageMode;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MailListener {

    private final MailService mailService;

    public MailListener(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(
            topics = "email_verification", groupId = "some", autoStartup = "true"
    )
    void listenEmailVerification(
            KafkaMailMessage kafkaMailMessage
    ) {
        mailService.send(kafkaMailMessage, MessageMode.EMAIL_VERIFICATION);
    }

    @KafkaListener(
            topics = "reset_password", groupId = "some", autoStartup = "true"
    )
    void listenResetPassword(
            KafkaMailMessage kafkaMailMessage
    ) {
        mailService.send(kafkaMailMessage, MessageMode.RESET_PASSWORD);
    }

    @KafkaListener(
            topics = "update_email", groupId = "some", autoStartup = "true"
    )
    void listenUpdateEmail(
            KafkaMailMessage kafkaMailMessage
    ) {
            mailService.send(kafkaMailMessage, MessageMode.UPDATE_EMAIL);
    }
}
