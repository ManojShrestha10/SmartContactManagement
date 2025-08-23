package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {
        // login with email and password
        if (authentication instanceof OAuth2AuthenticationToken) {

            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String userName = "";

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting login from google");
                userName = oauth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting emaail from github");
            }
            return userName;
        } else {
            System.out.println("Getting data from the local database");

        }
        return authentication.getName();

    }
    // Generate link for email verification
    public static String getLinkForEmailVerification(String emailToken) {
        // gennerate the link for email verification 
        String link = "http://localhost:8282/auth/verify-email?token=" + emailToken;
        // return the link 
        return link;

    }
}

