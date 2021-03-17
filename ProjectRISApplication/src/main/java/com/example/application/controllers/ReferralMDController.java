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
@RequestMapping(path="/referral") // This means URL's start with /demo (after Application path)
public class ReferralMDController {
    
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping("")
    public String referralIndexView(Model model)
    {
        model.addAttribute("patient", new Patient());
        model.addAttribute("patient_list", patientRepository.findAll());
        return "referral_index";
    }

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

    @GetMapping("/neworder/{patient_id}")
    public String orderView(Model model, @PathVariable("patient_id") Long patient_id, Principal principal)
    {
        
        Optional<Patient> find_patient = patientRepository.findById(patient_id);
        if(find_patient.isPresent())
        {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            
            Patient patient = find_patient.get();
            Order order = new Order();
            order.setPatient(patient_id);
            order.setReferral_md(((AppUserDetails)loggedInUser.getPrincipal()).getUserId());

            model.addAttribute("patient", patient);
            model.addAttribute("order", order);
        }
        return "order_form";
    }

    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute("order") Order order, Model model, BindingResult result)
    {

        orderRepository.save(order);
        

        return "redirect:/home";
    }
}