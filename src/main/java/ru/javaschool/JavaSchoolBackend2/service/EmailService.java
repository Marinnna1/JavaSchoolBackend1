package ru.javaschool.JavaSchoolBackend2.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);

    void sendMessage(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marinakrayukh@gmail.com");
        message.setTo(email);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);

        LOGGER.info("message sent");
    }

}
