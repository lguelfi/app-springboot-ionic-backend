package com.leonardo.springwebservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leonardo.springwebservice.domain.City;
import com.leonardo.springwebservice.repositories.CityRepository;

@Service
public class CityService {
    
    @Autowired
    private CityRepository cityRepository;

    public List<City> findCities(Integer stateId) {
        return cityRepository.findCities(stateId); 
    }
}
