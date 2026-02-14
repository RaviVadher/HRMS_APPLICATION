package com.roima.hrms.mail;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
