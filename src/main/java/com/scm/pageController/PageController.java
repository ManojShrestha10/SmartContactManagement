package com.scm.pageController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/home")
    public String Home(ModelMap model) {
        model.addAttribute("name", "Spring MVC");
        return "home";
    }
    @GetMapping("/about")
    public String aboutPage(){
        System.out.println("About page loading");
        return "about";
    }
    @GetMapping("/contact")
    public String contactPage(){
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
    
    
}
