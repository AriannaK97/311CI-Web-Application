package com.databases.project1.controller;

import com.databases.project1.entity.Incident;
import com.databases.project1.entity.RequestType;
import com.databases.project1.service.RequestTypeService;
import com.databases.project1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

    @Autowired
    private RequestTypeService requestTypeService;

    @GetMapping("/")
    public String showHomePage() {

        return "home2";

    }


}