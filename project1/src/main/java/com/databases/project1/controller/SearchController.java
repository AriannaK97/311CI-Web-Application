package com.databases.project1.controller;

import com.databases.project1.entity.Incident;
import com.databases.project1.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SearchController {

    @Autowired
    IncidentRepository incidentRepository;

    @GetMapping("/search")
    public String search() {

        UUID uuid = UUID.fromString("0e19d49d-3a16-5cbe-d18f-425968ff43f3");

        Optional<Incident> abandonedVehicle =  incidentRepository.findById(uuid);

        return "home";
    }
}
