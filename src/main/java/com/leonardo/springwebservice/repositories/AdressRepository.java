package com.leonardo.springwebservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leonardo.springwebservice.domain.Adress;

@Repository
public interface AdressRepository extends JpaRepository<Adress, Integer> {
    
}
    