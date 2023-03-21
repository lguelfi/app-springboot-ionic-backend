package com.leonardo.springwebservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.springwebservice.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    @Transactional(readOnly = true)
    Client findByEmail(String email);
}
    