package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthAuthenticaionSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OauthAuthenticaionSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("Oauth2 login success");

        // identify the user
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        // get the authorized client registration id
        // this is the provider name like google, facebook, github etc.
        String authorizationRegistrationID = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizationRegistrationID);

        // get the user details from the authentication object
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();

        oauth2User.getAttributes().forEach((key, value) -> {
            logger.info("{} => {} ", key, value);

        });
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        if (authorizationRegistrationID.equalsIgnoreCase("google")) {
            // google
            // google attributes
            user.setEmail(oauth2User.getAttribute("email").toString());
            user.setPassword("google");
            user.setProfilePic(oauth2User.getAttribute("picture").toString());
            user.setName(oauth2User.getAttribute("name").toString());
            user.setProvider(Providers.GOOGLE);
            user.setProviderUserId(oauth2User.getName());
            user.setAbout("Login using google");
        } else if (authorizationRegistrationID.equalsIgnoreCase("github")) {
            String email = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                    : oauth2User.getAttribute("login").toString() + "@github.com";
            String picture = oauth2User.getAttribute("avatar_url").toString();
            String name = oauth2User.getAttribute("login").toString();
            String providerUserId = oauth2User.getName();

            user.setEmail(email);
            user.setPassword("github");
            user.setProfilePic(picture);
            user.setName(name);
            user.setProvider(Providers.GITHUB);
            user.setProviderUserId(providerUserId);
            user.setAbout("Login using github");
        } else if (authorizationRegistrationID.equalsIgnoreCase("linkedin")) {
            // linkedin
        } else {
            logger.info("Unknown provider");
        }

        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user);

        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");

    };
}
