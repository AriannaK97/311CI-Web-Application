package com.databases.project1.controller;

import com.databases.project1.entity.Incident;
import com.databases.project1.repository.IncidentRepository;
import com.databases.project1.service.SearchRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SearchController {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    IncidentRepository incidentRepository;

    @Autowired
    SearchRequestService searchRequestService;

    @GetMapping("/search")
    public String search() {

        UUID uuid = UUID.fromString("0e19d49d-3a16-5cbe-d18f-425968ff43f3");

        int queryType = 1;

        Optional<Incident> abandonedVehicle =  incidentRepository.findById(uuid);


        if (queryType == 1) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date firstDate = format.parse("2018-03-07");
                Date secondDate = format.parse("2016-03-03");

                LocalDate first = LocalDate.parse("2015-09-10",dateTimeFormat);
                LocalDate second = LocalDate.parse("2016-03-18",dateTimeFormat);

                List<Object[]> list = incidentRepository.findtotalRequestsPerType(first, second);
                list = searchRequestService.findtotalRequestsPerType(first,second);

            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

        return "home";
    }
}
