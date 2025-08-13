package com.scm.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.scm.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    // Autowired JavaMailSender
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("shresthamanoj522@gmail.com");
        message.setSubject("This is world");
        message.setText("THis is textting");
        message.setFrom("shresthamanoj510@gmail.com");
        javaMailSender.send(message);
    }
}
