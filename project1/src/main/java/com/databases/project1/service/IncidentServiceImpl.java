package com.databases.project1.service;

import com.databases.project1.entity.Incident;
import com.databases.project1.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    public Incident findByRequestNumber(String requestNumber) {
        return incidentRepository.findByServiceRequestNumber(requestNumber).orElse(null);
    }

    public Incident saveIncident(Incident incident) {
        return (incidentRepository.save(incident));
    }





}
