package com.scm.services;

public interface EmailService {
    //Method to send an email
    void sendEmail(String to, String subject, String body);

}
