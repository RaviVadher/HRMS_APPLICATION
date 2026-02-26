package com.roima.hrms.mail;

import java.util.List;
import java.util.Set;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendMailWithAttachment(String to, String subject, String body, String filePath);
    void sendMailWithAttachmentALl(Set<String> to, String subject, String body, String filePath) ;
    void sendMailWithCalendar(String to, String subject, String body, String icsContent) ;

}
