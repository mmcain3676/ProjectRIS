package com.example.application.repositories;


import com.example.application.persistence.Order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
 
public interface OrderRepository extends CrudRepository<Order, Long> {
    
    @Query("SELECT c FROM Order c where c.referral_md=:referral_md")
    public Iterable<Order> findAllOrdersByReferralmd(@Param("referral_md") Long referralMd);
}