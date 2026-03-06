package com.roima.hrms.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "password_reset_otp")
public class PasswordResetOtp {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String otp;

      @Column(name="user_name")
      private  String userName;

      private LocalDateTime expiryTime;

      private int attempt;

      private boolean verified;

}
