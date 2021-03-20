package com.example.application.controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

import com.example.application.fileservice.FileResponse;
import com.example.application.fileservice.StorageService;
import com.example.application.persistence.Appointment;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping(path="/imaging") // This means URL's start with /admin (after Application path)
public class TechnicianController {

    private final String UPLOAD_DIR = "src/web/uploads";
    private StorageService storageService;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PatientRepository patientRepository;

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
                return "imaging_order";
            }
        }
        return "redirect:/imaging/appointments";
    }


    @GetMapping("/modality")
    public String homepage() {
        return "FileUpload";
    }
/*
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        System.out.println("Made it this far");
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        System.out.println(fileName);
        try 
        {
            System.out.println(saveFile(file, fileName));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        /*
        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/";
    }
*/
    
    @PostMapping("/upload")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return new FileResponse(name, uri, file.getContentType(), file.getSize());
    }

    private String saveFile(MultipartFile file, String fileName) throws IOException
    {
        FileOutputStream output = new FileOutputStream(UPLOAD_DIR + fileName);
        output.write(file.getBytes());

        return UPLOAD_DIR + fileName;
    }

}