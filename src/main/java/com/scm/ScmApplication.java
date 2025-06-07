package com.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//spring security is not used in this project so exclude it 
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class ScmApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScmApplication.class, args);
	}
}
