package com.roima.hrms.mail;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendMailWithAttachment(String to, String subject, String body, String filePath);
    void sendMailWithAttachmentALl(List<String> to, String subject, String body, String filePath) ;
    void sendMailWithCalendar(String to, String subject, String body, String icsContent) ;

}
