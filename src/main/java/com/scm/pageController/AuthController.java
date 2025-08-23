package com.scm.pageController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.scm.entities.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repositories.UserRepo;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepo userRepo;

    // Verify email
    @RequestMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        // Find user by verification token
        User user = userRepo.findByEmailToken(token);
        if (user != null) {
            if (user.getEmailToken().equals(token)) {
                // If user is found, set emailVerified to true
                user.setEmailVerified(true);
                // If user is found, set enabled to true
                user.setEnabled(true);
                // Save the updated user
                userRepo.save(user);
                // Redirect to success page or return success message
                session.setAttribute("message", Message.builder()
                        .type(MessageType.blue)
                        .content("Email verified successfully! You can now log in.")
                        .build());
                return "user/verification_success";
            }
            return "user/verification-error";
        }
        // Set message in session
        session.setAttribute("message", Message.builder()
                .type(MessageType.red)
                .content("Invalid verification token.")
                .build());
        return "user/verification-error";
    }

}
