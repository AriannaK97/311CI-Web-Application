package com.databases.project1.controller;

import com.databases.project1.service.RequestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

    @Autowired
    private RequestTypeService requestTypeService;

    @GetMapping("/")
    public String showHomePage() {

        return "home";

    }


}