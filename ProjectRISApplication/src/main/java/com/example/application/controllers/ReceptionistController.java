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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.example.application.dto.OrderDTO;
import com.example.application.persistence.Appointment;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.persistence.Role;
import com.example.application.persistence.User;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.UserRepository;

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
    private AppointmentRepository appointmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/appointments")
    public String appointmentView(HttpSession session, Model model)
    { 
        Iterable<Appointment> appointments_list = appointmentRepository.findAll();

        model.addAttribute("appointments_list", appointments_list);
        
        return "appointments";
    }

    @GetMapping("/orders")
    public String orderView(HttpSession session, Model model)
    {
        //  Create all timeslots

        ArrayList<String> times_list = new ArrayList<String>();
        int[] times = {8, 9, 10, 11, 12, 1, 2, 3, 4};
        for(int time : times)
        {
            if(time >= 8 && time < 12)
            {
                times_list.add(time + ":00am");
                times_list.add(time + ":30am");
            }
            else
            {
                times_list.add(time + ":00pm");
                times_list.add(time + ":30pm");
            }
        }

        //  Find doctors and technicians

        Iterable<User> users = userRepository.findAll();
        LinkedList<User> radiologists = new LinkedList<User>();
        LinkedList<User> technicians = new LinkedList<User>();

        for(User user : users)
        {
            for(Role role : user.getRoles())
            {
                System.out.println(role.getName());
                if(role.getRole_id() == 6)          //  Is user a Radiologist
                {
                    radiologists.add(user);
                }
                if(role.getRole_id() == 5)          //  Is user a Technician
                {
                    technicians.add(user);
                }
            }
        }

        //  Find all orders without appointments and add them to an OrdersDTO list (for patient and referral md info)

        Iterable<Order> all_orders = orderRepository.findAll();
        LinkedList<Order> orders = new LinkedList<Order>();

        for(Order order : all_orders)
        {
            if(order.getAppointment() == null || order.getAppointment() <= 0)
            {
                orders.add(order);
            }
        }
        
        ArrayList<OrderDTO> orderDTO_list = new ArrayList<OrderDTO>();
        for(Order order : orders)
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

        model.addAttribute("times_list", times_list);
        model.addAttribute("radiologists_list", radiologists);
        model.addAttribute("technicians_list", technicians);
        model.addAttribute("orders_list", orderDTO_list);
        model.addAttribute("appointment", new Appointment());

        return "orders_desk";
    }

    @PostMapping("/updateAppointment")
    public String updateAppointment(@ModelAttribute("appointment") Appointment appointment, Model model, BindingResult result)
    {

        appointment.setDatetime(appointment.getDate() + " " + appointment.getTime().substring(0, appointment.getTime().length() - 2));      //  Cut of the -am or -pm

        Appointment newAppointment = appointmentRepository.save(appointment);
        orderRepository.setAppointmentForOrder(newAppointment.getId(), newAppointment.getOrder());

        return "redirect:/staff/appointments";
    }

    @PostMapping("/checkinAppointment")
    public String checkinAppointment(@ModelAttribute("checkin_appointment") Appointment appointment, Model model, BindingResult result)
    {
        appointmentRepository.setCheckedInForAppointment(appointment.getId());
        return "redirect:/staff/appointments";
    }
}