package com.julant7.mailservice.listener;

import com.julant7.mailservice.service.MailService;
import com.julant7.mailservice.utils.KafkaMailMessage;
import com.julant7.mailservice.utils.MessageMode;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MailListener {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MailListener.class);
    private final MailService mailService;

    public MailListener(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(
            topics = "email_message", groupId = "some", autoStartup = "true"
    )
    void listen(
            KafkaMailMessage kafkaMailMessage
    ) {
        log.info("7777777777777777777777777777777777777");
        log.info("email message: {} ", kafkaMailMessage);
        mailService.send(kafkaMailMessage, MessageMode.EMAIL_VERIFICATION);
    }
}
