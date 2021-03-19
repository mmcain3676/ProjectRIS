package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import com.example.application.persistence.User;
import com.example.application.persistence.UsersRoles;
import com.example.application.persistence.UsersRolesList;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.RoleRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.repositories.UsersRolesRepository;

@Controller 
@RequestMapping(path="/staff") // This means URL's start with /admin (after Application path)
public class ReceptionistController {
    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/appointments")
    public String appointmentView(HttpSession session, Model model)
    {
        return "appointments";
    }

    @GetMapping("/orders")
    public String orderView(HttpSession session, Model model)
    {
        model.addAttribute("orders_list", orderRepository.findAll());
        return "orders";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, Model model, BindingResult result)
    {
 
        return "redirect:dashboard";
    }
}