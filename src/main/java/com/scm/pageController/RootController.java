package com.scm.pageController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helper.helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {
    Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLogedInUserInformatioon(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        } else {
            String username = helper.getEmailOfLoggedInUser(authentication);
            logger.info("User logged in: {}", username);
            User user = userService.getUserByEmail(username);
            if (user != null) {
                logger.info(user.getName());
                logger.info(user.getEmail());
                logger.info(user.getProfilePic());
                model.addAttribute("loggedInUser", user);

            } else {
                logger.info("Not logged in");
            }

        }
    }
}
