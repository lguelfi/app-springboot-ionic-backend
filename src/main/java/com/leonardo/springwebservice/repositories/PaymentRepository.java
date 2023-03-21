package com.leonardo.springwebservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leonardo.springwebservice.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
}
    