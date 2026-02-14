package com.roima.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HrmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmsApplication.class, args);
	}
}
