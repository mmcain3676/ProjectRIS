package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.persistence.PatientAlertsList;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.security.AppUserDetails;

@Controller 
@RequestMapping(path="/diagnotics") // This means URL's start with /demo (after Application path)
public class RadiologistController {
    
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/updatePatient")
    public String updatePatient(@ModelAttribute("patient") Patient patient, @ModelAttribute("alerts") PatientAlertsList alerts, Model model, BindingResult result)
    {
        Long patient_id;

        if(patient.getId() == null)     //  Create new patient
        {
            patient_id = patientRepository.save(patient).getId();
        }
        else                            //  Use existing patient with patient_id
        {
            patient_id = patient.getId();
        }

        return "redirect:/referral/neworder/" + patient_id;
    }

    @GetMapping("/order/{order_id}")
    public String orderView(Model model, @PathVariable("order_id") Long order_id, Principal principal)
    {
        
        Optional<Order> find_order = orderRepository.findById(order_id);
        if(find_order.isPresent())
        {

        }
        return "order_form";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute("order") Order order, Model model, BindingResult result)
    {

        order.setStatus(Long.valueOf(1));
        orderRepository.save(order);
        

        return "redirect:/home";
    }
}