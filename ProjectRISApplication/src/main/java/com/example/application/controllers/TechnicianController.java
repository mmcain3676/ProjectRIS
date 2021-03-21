package com.example.application.controllers;

import java.util.ArrayList;
import java.util.Optional;

import com.example.application.fileservice.StorageService;
import com.example.application.persistence.Appointment;
import com.example.application.persistence.FileUpload;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path="/imaging") // This means URL's start with /admin (after Application path)
public class TechnicianController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;

    private StorageService storageService;

    public TechnicianController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/appointments")
    public String appointments(Model model)
    {
        Iterable<Appointment> appointments_list = appointmentRepository.findAll();

        //  Find checked-in/closed appointments

        ArrayList<Appointment> checked_in_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list)
        {
            if(appointment.getCheckedin() == 1)
            {
                checked_in_appointments_list.add(appointment);
            }
        }
        
        model.addAttribute("checked_in_appointments_list", checked_in_appointments_list);

        return "imaging_appointments";
    }

    @GetMapping("/order/{order_id}")
    public String completeOrder(@PathVariable Long order_id, Model model)
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
                return "imaging_order";
            }
        }
        return "redirect:/imaging/appointments";
    }
    
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, @ModelAttribute("order") Order order) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String filetype = filename.substring(filename.lastIndexOf(".") + 1);

        FileUpload fileUpload = new FileUpload();
        fileUpload.setFilename(filename);
        fileUpload.setFiletype(file.getContentType());
        fileUpload.setActive(1);
        fileUpload.setOrder(order.getId());

        fileUpload = fileUploadRepository.save(fileUpload);
        fileUpload.setFilename("RIS_" + fileUpload.getId() + "." + filetype);

        fileUpload = fileUploadRepository.save(fileUpload);

        storageService.store(file, fileUpload.getFilename());

        return "redirect:/imaging/order/" + order.getId();
    }

    @PostMapping("completeOrder")
    public String completeOrder(@ModelAttribute("order") Order order){
        
        orderRepository.setStatusForOrder(Long.valueOf(2), order.getId());
        return "redirect:/imaging/appointments";
    }

}