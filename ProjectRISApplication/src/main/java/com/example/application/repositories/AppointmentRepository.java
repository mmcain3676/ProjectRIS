package com.example.application.repositories;


import com.example.application.persistence.Appointment;

import org.springframework.data.repository.CrudRepository;
 
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    
}