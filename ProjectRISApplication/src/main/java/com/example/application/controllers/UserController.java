package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.example.application.dto.OrderDTO;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.persistence.User;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.security.AppUserDetails;

@Controller 
//@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class UserController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/home")
    public String homeView(HttpSession session, Model model)
    {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        /*      Create OrderDTO (Order class data transfer object) to allow for Patient and User (Referral MD) objects to be passed     */
        
        Iterable<Order> order_list = orderRepository.findAllOrdersByReferralmd(((AppUserDetails) loggedInUser.getPrincipal()).getUserId());
        ArrayList<OrderDTO> orderDTO_list = new ArrayList<OrderDTO>();
        for(Order order : order_list)
        {
            Optional<Patient> patientObject = patientRepository.findById(order.getPatient());
            Optional<User> referral_mdObject = userRepository.findById(order.getReferral_md());

            if(patientObject.isPresent() && referral_mdObject.isPresent())
            {
                OrderDTO new_order = new OrderDTO(
                    order.getId(),
                    patientObject.get(),
                    referral_mdObject.get(),
                    order.getModality(),
                    order.getNotes(),
                    order.getStatus(),
                    order.getReport(),
                    order.getAppointment());
    
                new_order.setId(order.getId());
    
                orderDTO_list.add(new_order);
            }
        }

        model.addAttribute("orders_list", orderDTO_list);

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