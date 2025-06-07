package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.scm.services.Impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {
    /*
     * @Bean
     * public UserDetailsService userDetailsService() {
     * PasswordEncoder encoder =
     * PasswordEncoderFactories.createDelegatingPasswordEncoder();
     * UserDetails user = User
     * .withUsername("admin")
     * .password("admin1")
     * .passwordEncoder(encoder::encode)
     * .roles("ADMIN", "USER")
     * .build();
     * 
     * var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user);
     * return inMemoryUserDetailsManager;
     * 
     * }
     */
    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
