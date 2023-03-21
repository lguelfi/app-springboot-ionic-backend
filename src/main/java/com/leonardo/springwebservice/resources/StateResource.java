package com.leonardo.springwebservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.springwebservice.domain.City;
import com.leonardo.springwebservice.domain.State;
import com.leonardo.springwebservice.domain.dto.CityDTO;
import com.leonardo.springwebservice.domain.dto.StateDTO;
import com.leonardo.springwebservice.services.CityService;
import com.leonardo.springwebservice.services.StateService;


@RestController
@RequestMapping(value = "/states")
public class StateResource {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;
    
    @GetMapping
    public ResponseEntity<List<StateDTO>> findAll() {
        List<State> states = stateService.findAll();
        List<StateDTO> statesDto = states.stream().map(x -> new StateDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(statesDto);
    }

    @GetMapping(value="/{stateId}/cities")
    public ResponseEntity<List<CityDTO>> findCities(@PathVariable Integer stateId) {
        List<City> cities = cityService.findCities(stateId);
        List<CityDTO> citiesDto = cities.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(citiesDto);
    }
}
