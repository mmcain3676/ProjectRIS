package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.application.persistence.Patient;
import com.example.application.persistence.PatientAlertsList;
import com.example.application.repositories.PatientRepository;

@Controller 
@RequestMapping(path="/referral") // This means URL's start with /demo (after Application path)
public class ReferralMDController {
    
    @Autowired
    PatientRepository patientRepository;

    @GetMapping("")
    public String referralIndexView(Model model)
    {
        model.addAttribute("patient", new Patient());
        model.addAttribute("patient_list", patientRepository.findAll());
        return "referral_index";
    }

    @GetMapping("/neworder/{order_id}")
    public String orderView(Model model, @PathVariable("order_id") Long order_id)
    {
        System.out.println(order_id);
        return "home";
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
}