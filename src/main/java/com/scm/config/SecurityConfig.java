package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

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

    @Autowired
    private OauthAuthenticaionSuccessHandler oauthAuthenticaionSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    // configuration for authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // configuration for http security
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/login").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });
        // form default login
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(authFailureHandler);
            
    
        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // oauth2 login configuration
        httpSecurity.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/login");
            oauth2Login.successHandler(oauthAuthenticaionSuccessHandler);

        });

        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
