package com.scm.pageController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;
import jakarta.validation.Valid;

@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String Home(ModelMap model) {
        model.addAttribute("name", "Spring MVC");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage() {
        System.out.println("About page loading");
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        System.out.println("Contact page loading");
        return "contact";
    }

    @GetMapping("/navbar")
    public String navbarPage() {
        return "navbar";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/services")
    public String getMethodName() {
        return new String("Services");
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    // signup register
    @PostMapping(value = "/do-Register")
    public String doRegister(@Valid @ModelAttribute UserForm userForm, BindingResult result,
            RedirectAttributes redirectAttributes) {
        // userService
        /*
         * User user = User.builder()
         * .name(userForm.getName())
         * .email(userForm.getEmail())
         * .password(userForm.getPassword())
         * .phoneNumber(userForm.getPhoneNumber())
         * .about(userForm.getAbout())
         * .profilePic(
         * "https://www.google.com/search?q=messi&rlz=1C5CHFA_enJP1058JP1058&oq=messi&gs_lcrp=EgZjaHJvbWUqBwgAEAAYjwIyBwgAEAAYjwIyDAgBEC4YQxiABBiKBTIMCAIQABhDGIAEGIoFMgwIAxAAGEMYgAQYigUyBwgEEAAYgAQyBwgFEAAYgAQyBwgGEAAYgAQyBwgHEAAYgAQyBwgIEAAYgAQyBwgJEAAYjwLSAQgxMjczajBqN6gCALACAA&sourceid=chrome&ie=UTF-8#vhid=eRC3qNx8JSk27M&vssid=_S-0-aISqCY6B2roPt-ndkA4_61")
         * .build();
         */
        // validation form data
        if (result.hasErrors()) {
            return "register";
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setAbout(userForm.getAbout());
        user.setProfilePic(
                "https://www.google.com/search?q=messi&rlz=1C5CHFA_enJP1058JP1058&oq=messi&gs_lcrp=EgZjaHJvbWUqBwgAEAAYjwIyBwgAEAAYjwIyDAgBEC4YQxiABBiKBTIMCAIQABhDGIAEGIoFMgwIAxAAGEMYgAQYigUyBwgEEAAYgAQyBwgFEAAYgAQyBwgGEAAYgAQyBwgHEAAYgAQyBwgIEAAYgAQyBwgJEAAYjwLSAQgxMjczajBqN6gCALACAA&sourceid=chrome&ie=UTF-8#vhid=eRC3qNx8JSk27M&vssid=_S-0-aISqCY6B2roPt-ndkA4_61");

        userService.saveUser(user);
        Message message = Message.builder().content("Registration successfull").type(MessageType.blue).build();
        System.out.println(message.getContent());
        // add message
        // it will
        redirectAttributes.addFlashAttribute("message", message);
        System.out.println("User saved");
        return "redirect:/register";
    }
}
