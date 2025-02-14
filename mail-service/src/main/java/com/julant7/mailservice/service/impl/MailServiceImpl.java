package com.julant7.mailservice.service.impl;

import com.julant7.mailservice.service.MailService;
import com.julant7.mailservice.utils.KafkaMailMessage;
import com.julant7.mailservice.utils.MessageMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${server.frontend-url}")
    private String frontEndURL;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(KafkaMailMessage kafkaMailMessage, MessageMode mode) {
        var msg = new SimpleMailMessage();

        if (mode == MessageMode.EMAIL_VERIFICATION) {
            msg.setText(frontEndURL + "/register?data=" + kafkaMailMessage.message());
        } else if (mode == MessageMode.RESET_PASSWORD) {
            msg.setText(frontEndURL + "/reset_password?data=" + kafkaMailMessage.message());
        } else if (mode == MessageMode.UPDATE_EMAIL) {
            msg.setText(frontEndURL + "/update_email?data=" + kafkaMailMessage.message());
        }
        msg.setSubject("Verify your email address");
        msg.setTo(kafkaMailMessage.email());
        msg.setFrom(mailUsername);
        log.info(mailUsername);
        try {
            mailSender.send(msg);
            log.info("Email send, msg: {}, mode: {}", kafkaMailMessage, mode);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
    }
}
