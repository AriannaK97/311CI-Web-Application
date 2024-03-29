package com.databases.project1.controller;

import com.databases.project1.dto.SearchDto;
import com.databases.project1.entity.Incident;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.repository.IncidentRepository;
import com.databases.project1.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/search")
public class SearchController {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    IncidentRepository incidentRepository;


    @Autowired
    SearchService searchService;

    @PostMapping("/processSearch")
    public String processSearch(@ModelAttribute("searchDto") SearchDto searchDto, Model theModel, HttpServletRequest request) {

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");
        int searchType = searchDto.getSearchType();
        List<Object[]> objectList = null;
        List<Incident> incidentList = null;
        LocalDateTime startTime = LocalDateTime.now();
        try {
            if (searchType == 1) {
                objectList = searchService.findTotalRequestsPerType(searchDto, user);
            }

            else if (searchType == 2 ) {
                objectList = searchService.findTotalRequestsPerDayAndType(searchDto, user);
            }

            else if (searchType == 3) {
                objectList = searchService.findMostCommonServiceRequestPerZipCode(searchDto, user);
            }

            else if (searchType == 4) {
                objectList = searchService.findAvgCompletionTimePerServiceReqType(searchDto, user);
                for (Object[] object : objectList) {
                    BigDecimal num = (BigDecimal) object[0];
                    object[0] = num.setScale(2, RoundingMode.DOWN);
                }
            }

            else if (searchType == 5) {
                objectList = searchService.findMostCommonReqType(searchDto, user);
            }

            else if (searchType == 6) {
                objectList = searchService.findTopSSAs(searchDto, user);
            }

            else if (searchType == 7) {
                objectList = searchService.findNotoriousPlates(searchDto, user);
            }

            else if (searchType == 8) {
                objectList = searchService.findSecondMostUsualVehicleColor(searchDto, user);
            }

            else if (searchType == 9) {
                incidentList = searchService.findRodentBaitingRequestsByBaitedPremises(searchDto, user);
            }

            else if (searchType == 10) {
                incidentList = searchService.findRodentBaitingRequestsByGarbagePremises(searchDto, user);
            }

            else if (searchType == 11) {
                incidentList = searchService.findRodentBaitingRequestsByRatPremises(searchDto, user);
            }

            else if (searchType == 12) {
                objectList = searchService.findBusyPoliceDistricts(searchDto, user);
            }

            else if (searchType == 13) {
                incidentList = searchService.findByZipCode(searchDto, user);
            }

            else if (searchType == 14) {
                incidentList = searchService.findByStreetAddress(searchDto, user);
            }

            else if (searchType == 15) {
                incidentList = searchService.findByStreetAddressAndZipCode(searchDto, user);
            }

            else {
                incidentList = searchService.findAll(searchDto, user);
            }

            if (objectList != null) {
                theModel.addAttribute("objectList", objectList);
            }

            else if (incidentList != null) {
                theModel.addAttribute("incidentList", incidentList);
            }

            else {
                theModel.addAttribute("error", "no result list is available");
            }

            Long executionTime = startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
            Long seconds = executionTime / 1000;
            Long milliseconds = executionTime % 1000;
            System.out.println("Execution time of query " + searchDto.getSearchType() + " was " + seconds + " seconds and " + milliseconds + " milliseconds");

            theModel.addAttribute("searcher",searchDto);


        } catch (Exception e) {
            e.printStackTrace();
            theModel.addAttribute("error", "no result list is available");
        }


        return "search";
    }

    @GetMapping("/showSearch")
    public String search(Model theModel) {
        SearchDto searcher = new SearchDto();
        theModel.addAttribute("searcher", new SearchDto());
        return "search";
    }



}
