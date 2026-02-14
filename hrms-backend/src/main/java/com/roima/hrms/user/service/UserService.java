package com.roima.hrms.user.service;

import com.roima.hrms.auth.dto.LoginRequestDTO;
import com.roima.hrms.auth.dto.RegisterRequestDTO;
import com.roima.hrms.user.dto.UserMapper;
import com.roima.hrms.user.dto.UserResponceDto;
import com.roima.hrms.user.entity.Role;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.RoleRepository;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

   //Register methode
    public User register(RegisterRequestDTO registerRequestDTO) {

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");

        }

        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(registerRequestDTO.getRole());
        return userRepository.save(user);
    }

    //find existing user by mail
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    public List<UserResponceDto> getAllUser() {
         return userRepository.findAll() .stream()
                .map(UserMapper::toDto)
                .toList();

    }
}
