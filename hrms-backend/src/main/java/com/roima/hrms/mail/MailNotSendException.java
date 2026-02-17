package com.roima.hrms.mail;

public class MailNotSendException extends RuntimeException {
    public MailNotSendException(String message) {
        super(message);
    }
}
