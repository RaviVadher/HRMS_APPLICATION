package com.roima.hrms.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
      try{
          SimpleMailMessage message = new SimpleMailMessage();
          message.setTo(to);
          message.setSubject(subject);
          message.setText(body);

          mailSender.send(message);
      }
      catch (Exception e){
          System.err.println(e.getMessage() + "Email failed to send");
      }
    }
}
