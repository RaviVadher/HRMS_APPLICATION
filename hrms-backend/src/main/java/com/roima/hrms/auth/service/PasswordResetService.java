package com.roima.hrms.auth.service;

public interface PasswordResetService {
    void sendOtp(String userName);
    void verifyOtp(String userName,String enteredOtp);
     void resetPassword(String userName,String newPassword);

}
