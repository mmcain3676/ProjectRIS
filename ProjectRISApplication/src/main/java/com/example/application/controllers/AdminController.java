package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.application.persistence.User;
import com.example.application.repositories.RoleRepository;
import com.example.application.repositories.UserRepository;

@Controller 
//@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class AdminController {
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private RoleRepository roleRepository;

    @GetMapping("/admin")
    public String homeView(HttpSession session, Model model)
    {
        if (isAuthenticated()) {
            model.addAttribute("users_list", userRepository.findAll());
            model.addAttribute("roles_list", roleRepository.findAll());
            return "admin_dashboard";
        }
        else
        {
            model.addAttribute("user", new User());
            return "redirect:login";
        }
    }

    /*
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
*/
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}