package com.scm.pageController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    // user dashboard page
    @GetMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }
    //user profile page
    @GetMapping(value = "/profile")
    public String userProfile() {
        return "user/profile";
    }

}
