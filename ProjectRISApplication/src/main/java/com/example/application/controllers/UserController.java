package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
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
import com.example.application.security.AppUserDetails;

@Controller 
public class UserController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @GetMapping("/home")
    public String homeView(HttpSession session, Model model)
    {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        //////////////////////////////      REFERRAL MD DASHBOARD       //////////////////////////////
        
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

        //////////////////////////////      RECEPTIONIST DASHBOARD      //////////////////////////////

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

        //  Find today's appointments

        ArrayList<Appointment> todays_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list) 
        {
            String datetime = appointment.getDatetime();
            String date = datetime.split(" ")[0];
            String today = LocalDate.now().toString();

            if(date.equals(today) && appointment.getCheckedin() != 1)
            {
                todays_appointments_list.add(appointment);
            }
        }

        
        model.addAttribute("appointments_list", appointments_list);
        model.addAttribute("todays_appointments_list", todays_appointments_list);
        model.addAttribute("checked_in_appointments_list", checked_in_appointments_list);
        model.addAttribute("checkin_appointment", new Appointment());


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
        
        ArrayList<OrderDTO> unscheduled_orderDTO_list = new ArrayList<OrderDTO>();
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
    
                unscheduled_orderDTO_list.add(new_order);
            }
        }

        model.addAttribute("times_list", times_list);
        model.addAttribute("radiologists_list", radiologists);
        model.addAttribute("technicians_list", technicians);
        model.addAttribute("unscheduled_orders_list", unscheduled_orderDTO_list);
        model.addAttribute("appointment", new Appointment());

        //////////////////////////////      RADIOLOGIST DASHBOARD   //////////////////////////////

        ArrayList<OrderDTO> complete_imaging_orders_list = new ArrayList<OrderDTO>();

        for(Order order : all_orders)
        {
            Optional<Patient> patientObject = patientRepository.findById(order.getPatient());
            Optional<User> referral_mdObject = userRepository.findById(order.getReferral_md());
            if(order.getStatus() == Long.valueOf(2) && patientObject.isPresent() && referral_mdObject.isPresent())
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

                complete_imaging_orders_list.add(new_order);
            }
        }

        model.addAttribute("complete_imaging_orders_list", complete_imaging_orders_list);










        return "home";
    }

    @GetMapping("/appointments")
    public String getAppointments(Model model) 
    {
        Iterable<Appointment> appointments_list = appointmentRepository.findAll();

        model.addAttribute("appointments_list", appointments_list);

        return "appointments";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) 
    {
        Iterable<Order> orders = orderRepository.findAll();
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

        model.addAttribute("orders_list", orderDTO_list);

        return "orders";
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


// NEW STUFF