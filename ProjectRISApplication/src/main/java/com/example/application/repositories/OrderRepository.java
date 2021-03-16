package com.example.application.repositories;


import com.example.application.persistence.Order;

import org.springframework.data.repository.CrudRepository;
 
public interface OrderRepository extends CrudRepository<Order, Long> {
 
    
}