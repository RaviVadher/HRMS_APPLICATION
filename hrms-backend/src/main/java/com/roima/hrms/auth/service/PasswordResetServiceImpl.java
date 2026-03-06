package com.roima.hrms.auth.service;

import com.roima.hrms.auth.entity.PasswordResetOtp;
import com.roima.hrms.auth.repository.PasswordResetRepository;
import com.roima.hrms.gamescheduling.exception.NotFoundException;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{

    private static final int MAX_ATTEMPTS = 3;

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private  final PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(PasswordResetRepository passwordResetRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    EmailService emailService) {
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateOtp(){

        Random random = new Random();
        int otp = 100000 + random.nextInt((900000));
        return String.valueOf(otp);
    }

    @Override
    public void sendOtp(String userName)
    {
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String otp = generateOtp();
        PasswordResetOtp resetOpt = new PasswordResetOtp();
        resetOpt.setUserName(user.getEmail());
        resetOpt.setOtp(otp);
        resetOpt.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        resetOpt.setAttempt(0);
        resetOpt.setVerified(false);

        emailService.sendEmail("ravivadher141@gmail.com","Reset password otp","Reset password otp:"+otp + "expire in 1 minute");
        passwordResetRepository.save(resetOpt);
    }

    @Override
    public void verifyOtp(String userName,String enteredOtp)
    {
        PasswordResetOtp resetOpt = passwordResetRepository.findByUserName(userName).orElseThrow(()-> new NotFoundException("otp not found"));

        if(resetOpt.getExpiryTime().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("otp expired");
        }

        if(resetOpt.getAttempt()>=MAX_ATTEMPTS)
        {
            throw new RuntimeException("max attempt reached");
        }

        if(!resetOpt.getOtp().equals(enteredOtp))
        {
            resetOpt.setAttempt(resetOpt.getAttempt()+1);
            passwordResetRepository.save(resetOpt);
            throw new RuntimeException("otp not match");
        }

        resetOpt.setVerified(true);
        passwordResetRepository.save(resetOpt);
    }


   @Override
   @Transactional
    public void resetPassword(String userName,String newPassword)
   {
       PasswordResetOtp resetOpt = passwordResetRepository.findByUserName(userName).orElseThrow(()-> new NotFoundException("otp not found"));

       if(!resetOpt.isVerified())
       {
           throw new RuntimeException("otp not verified");
       }

       User user = userRepository.findByEmail(userName).orElseThrow(()-> new UsernameNotFoundException("user not found"));

       user.setPassword(passwordEncoder.encode(newPassword));
       userRepository.save(user);
       passwordResetRepository.deleteByUserName(userName);
   }
}
