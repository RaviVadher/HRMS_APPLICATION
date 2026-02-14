package com.roima.hrms.auth.controller;

import com.roima.hrms.auth.dto.LoginRequestDTO;
import com.roima.hrms.auth.dto.RegisterRequestDTO;
import com.roima.hrms.auth.jwt.JwtUtil;
import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.mail.EmailTemplate;
import com.roima.hrms.user.dto.UserResponceDto;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserService userService, EmailService emailService) {
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtUtil.generateToken(principal);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        userService.register(request);
        return ResponseEntity.ok("user register successfully");
    }

    @GetMapping("/getEmployee")
    public List<UserResponceDto> getAllUsers() {
         return userService.getAllUser();
    }
}