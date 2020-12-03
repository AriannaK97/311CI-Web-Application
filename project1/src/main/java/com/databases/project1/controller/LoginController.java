package com.databases.project1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showCustomLoginPage() {

        return "customLogin";

    }

    // add request mapping for /access-denied

   @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "home";

    }

}
