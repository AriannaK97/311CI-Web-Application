package com.databases.project1.controller;

import com.databases.project1.entity.Incident;
import com.databases.project1.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IncidentController {
    @Autowired
    private IncidentService incident;

    @GetMapping(path="/RequestBoundingBoxForADay")
    public List<Incident> findTopIncidentTypeForSpecificBoundedBoxOnAGivenDate(@RequestParam("userlocation") List<Float> userLocation){

        float userLongitude = userLocation.get(0);
        float userLatitude = userLocation.get(1);

        return incident.findTopIncidentTypeForSpecificBoundedBoxOnAGivenDate(userLongitude, userLatitude);
    }

}
