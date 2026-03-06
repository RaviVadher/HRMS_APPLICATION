package com.roima.hrms.auth.controller;

import com.roima.hrms.auth.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

   private final PasswordResetService passwordResetService;
   public  PasswordResetController(PasswordResetService passwordResetService) {
       this.passwordResetService = passwordResetService;
   }

   @PostMapping("/fogot-password")
   public ResponseEntity<String > forgotPassword(@RequestParam String userName)
   {
       passwordResetService.sendOtp(userName);
       return  ResponseEntity.ok("Otp sent to register email");
   }

   @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String userName,@RequestParam String otp)
   {
       passwordResetService.verifyOtp(userName,otp);
       return ResponseEntity.ok("Otp verified");
   }

   @PostMapping("/reset-password")
    public  ResponseEntity<String> resetPassword(@RequestParam String userName,@RequestParam String newPassword)
   {
       passwordResetService.resetPassword(userName,newPassword);
       return  ResponseEntity.ok("Password reset successfully");
   }
}
