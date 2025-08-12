package com.scm.pageController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.scm.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    // user dashboard page
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }
    // getmapping
    @RequestMapping(value = "/profile")
    public String userProfile(Model model) {
        return "user/profile";
    }
    // user profile page
    @PostMapping(value = "/profile")
    public String userProfile() {
        return "user/profile";
    }
}
