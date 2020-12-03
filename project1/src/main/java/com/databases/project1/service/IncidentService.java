package com.databases.project1.service;

import com.databases.project1.entity.Incident;
import javassist.NotFoundException;

import java.util.List;

public interface IncidentService {

    Incident findByRequestNumber(String requestNumber);

}
