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

        int queryType = 1;
        List<Object[]> list;
        LocalDate first = LocalDate.parse("2015-09-10", dateTimeFormat);
        LocalDate second = LocalDate.parse("2016-03-18", dateTimeFormat);
        LocalDate standard = LocalDate.parse("2017-01-18", dateTimeFormat);
        try {
            if (queryType == 1) {
                list = incidentRepository.findtotalRequestsPerType(first, second);
                //list = searchRequestService.findtotalRequestsPerType(first, second);
            }

            else if (queryType == 3) {
                list = incidentRepository.findMostCommonServiceRequestPerZipCode(standard);
                int x = 5;
            }

            else if (queryType == 4) {

            }

            else if (queryType == 9) {
                List<Incident> ilist = incidentRepository.findRodentBaitingRequestsByBaitedPremises(2);
                int x = 5;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "home";
    }
}
