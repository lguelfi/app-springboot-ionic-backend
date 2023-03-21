package com.leonardo.springwebservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.springwebservice.domain.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    
    @Transactional(readOnly = true)
    @Query("From City WHERE state_id = ?1")
    List<City> findCities(Integer stateId);
}
    