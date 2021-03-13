package com.example.application.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

import com.example.application.persistence.User;

@Controller 
//@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class UserController {

    @GetMapping("/home")
    public String homeView(HttpSession session, Model model)
    {
        return "home";
    }

    @GetMapping("/login")
    public String getUserLoginPage(Model model) {
        if (isAuthenticated()) {
            return "redirect:home";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/403")
    public String accessDenied()
    {
        return "access_denied";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}