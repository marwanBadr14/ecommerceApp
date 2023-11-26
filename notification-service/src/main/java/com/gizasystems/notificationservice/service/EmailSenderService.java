package com.gizasystems.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String toEmail, String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gizaecommerce0@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject("Your New Order From Giza Ecommerce!");

        mailSender.send(message);
    }

}