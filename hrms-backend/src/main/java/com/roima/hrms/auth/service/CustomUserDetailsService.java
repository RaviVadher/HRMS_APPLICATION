package com.roima.hrms.auth.service;

import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
    {
         User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
         return new UserPrincipal(user);
    }
}
