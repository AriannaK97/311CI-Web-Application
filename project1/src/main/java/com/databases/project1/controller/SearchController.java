package com.databases.project1.controller;

import com.databases.project1.entity.Incident;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.repository.IncidentRepository;
import com.databases.project1.service.SearchRequestService;
import com.databases.project1.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class SearchController {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    IncidentRepository incidentRepository;

    @Autowired
    SearchRequestService searchRequestService;

    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public String search(Model theModel, HttpServletRequest request) {

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");

        int queryType = 1;
        List<Object[]> list;
        LocalDate first = LocalDate.parse("2015-09-10", dateTimeFormat);
        LocalDate second = LocalDate.parse("2016-03-18", dateTimeFormat);
        LocalDate standard = LocalDate.parse("2017-01-18", dateTimeFormat);
        int page = 0;
        int pagesize = 20;
        try {
            if (queryType == 1) {
                list = incidentRepository.findTotalRequestsPerType(first, second);
                theModel.addAttribute("list", list);
                //list = searchRequestService.findtotalRequestsPerType(first, second);
            }

            else if (queryType == 3) {
                list = incidentRepository.findMostCommonServiceRequestPerZipCode(standard,page , pagesize);
                int x = 5;
            }

            else if (queryType == 4) {

            }

            else if (queryType == 9) {
                List<Incident> ilist = incidentRepository.findRodentBaitingRequestsByBaitedPremises(2, page, pagesize);
                int x = 5;
            }

            else if (queryType == 13) {
                List<Incident> anotherList = incidentRepository.findByZipCode(60601, page, pagesize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "home";
    }
}
