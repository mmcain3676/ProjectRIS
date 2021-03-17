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
@RequestMapping(path="/admin") // This means URL's start with /admin (after Application path)
public class AdminController {
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private RoleRepository roleRepository;
    @Autowired
    private UsersRolesRepository usersRolesRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String homeView(HttpSession session, Model model)
    {
        UsersRolesList rolesList = new UsersRolesList();

        model.addAttribute("roles", rolesList);
        model.addAttribute("users_list", userRepository.findAll());
        model.addAttribute("roles_list", roleRepository.findAll());
        model.addAttribute("patients_list", patientRepository.findAll());
        model.addAttribute("orders_list", orderRepository.findAll());
        model.addAttribute("user", new User());
        return "admin_dashboard";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, @ModelAttribute("roles") UsersRolesList users_roles, Model model, BindingResult result)
    {

        if(user.getUser_id() == null)       //  Create new user if user_id == null
        {
            userRepository.save(user);
        } 
        else                                //  Update use on user_id
        {
            Optional<User> find_user = userRepository.findById(user.getUser_id());
            if(find_user.isPresent())       //Find current user
            {
                if(user.getPassword().isEmpty())        //See if we are changing the password
                {
                    user.setPassword(find_user.get().getPassword());       //If not, use old password
                }
                else
                {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));       //If so, encode new password
                }
            }
    
    
            usersRolesRepository.deleteByUserid(user.getUser_id());       //Delete old user roles
    
            userRepository.save(user);      //Save user first
    

            if(users_roles.getUsers_roles() == null)      // If no role is passed, create a default user (2) role
            {
                System.out.println("No role, adding user");
                UsersRoles default_user = new UsersRoles();
                default_user.setUserid(user.getUser_id());
                default_user.setRole_id(Long.valueOf(2));
                ArrayList<UsersRoles> usersRoleList = new ArrayList<UsersRoles>();
                users_roles.setUsers_roles(usersRoleList);
            }
    
    
            for(UsersRoles role : users_roles.getUsers_roles())
            {
                if(role.getRole_id() != null)
                {
                    role.setUserid(user.getUser_id());
                    usersRolesRepository.save(role);      //Save lise of roles
                }
            }
        }
 
        return "redirect:dashboard";
    }
}