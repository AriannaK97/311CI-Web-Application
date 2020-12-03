package com.databases.project1.controller;

import com.databases.project1.dto.SearchDto;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.repository.IncidentRepository;
import com.databases.project1.service.SearchRequestService;
import com.databases.project1.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String search(@ModelAttribute("searchDto") SearchDto searchDto, Model theModel, HttpServletRequest request) {

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");
        int searchType = searchDto.getSearchType();
        try {
            if (searchType == 1) {
                searchService.findTotalRequestsPerType(searchDto, user);
            }

            else if (searchType == 2 ) {
                searchService.findTotalRequestsPerDayAndType(searchDto, user);
            }

            else if (searchType == 3) {
                searchService.findMostCommonServiceRequestPerZipCode(searchDto, user);
            }

            else if (searchType == 4) {
                searchService.findAvgCompletionTimePerServiceReqType(searchDto, user);
            }

            else if (searchType == 5) {
                searchService.findMostCommonReqType(searchDto, user);
            }

            else if (searchType == 6) {
                searchService.findTopSSAs(searchDto, user);
            }

            else if (searchType == 7) {
                searchService.findNotoriousPlates(searchDto, user);
            }

            else if (searchType == 8) {
                searchService.findSecondMostUsualVehicleColor(searchDto, user);
            }

            else if (searchType == 9) {
                searchService.findRodentBaitingRequestsByBaitedPremises(searchDto, user);
            }

            else if (searchType == 10) {
                searchService.findRodentBaitingRequestsByGarbagePremises(searchDto, user);
            }

            else if (searchType == 11) {
                searchService.findRodentBaitingRequestsByRatPremises(searchDto, user);
            }

            else if (searchType == 12) {
                searchService.findBusyPoliceDistricts(searchDto, user);
            }

            else if (searchType == 13) {
                searchService.findByZipCode(searchDto, user);
            }

            else {
                  searchService.findAll(searchDto, user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return "search";
    }

    @GetMapping("/search")
    public String search2(Model theModel) {
        SearchDto searcher = new SearchDto();
        theModel.addAttribute("searcher", new SearchDto());
        return "search";
    }

    @GetMapping("/processSearch")
    public String processSearchQuery(@ModelAttribute("searcher") SearchDto searcher,
                                     BindingResult theBindingResult, Model theModel,
                                     @RequestParam(value = "creation_date", required = false, defaultValue = "") String creation_date){

        List<Object[]> list;
        /*LocalDate first = LocalDate.parse("2015-09-10", dateTimeFormat);
        LocalDate second = LocalDate.parse("2016-03-18", dateTimeFormat);
        LocalDate standard = LocalDate.parse("2017-01-18", dateTimeFormat);*/
        creation_date = searcher.getCreationDate();
        theModel.addAttribute("creation_date", creation_date);
        return "search";
    }

}
