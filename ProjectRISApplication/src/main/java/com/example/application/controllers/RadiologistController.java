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

import java.security.Principal;
import java.util.Optional;

import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.persistence.PatientAlertsList;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;

@Controller 
@RequestMapping(path="/diagnostics") // This means URL's start with /demo (after Application path)
public class RadiologistController {
    
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;

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
        Optional<Order> get_order = orderRepository.findById(order_id);
        if(get_order.isPresent())
        {
            Optional<Patient> get_patient = patientRepository.findById(get_order.get().getPatient());
            if(get_patient.isPresent())
            {
                Order order = get_order.get();
                Patient patient = get_patient.get();

                model.addAttribute("order", order);
                model.addAttribute("patient", patient);
                model.addAttribute("file_uploads_list", fileUploadRepository.getAllFileUploadsByOrderId(order.getId()));
                return "diagnostics_form";
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute("order") Order order, Model model, BindingResult result)
    {

        order.setStatus(Long.valueOf(1));
        orderRepository.save(order);
        

        return "redirect:/home";
    }
}