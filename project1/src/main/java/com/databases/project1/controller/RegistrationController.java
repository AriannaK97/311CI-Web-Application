package com.databases.project1.controller;

import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

//Todo: jnfjewfnje

@Controller
public class RegistrationController {
	
    @Autowired
    private UserService userService;
	
    private Logger logger = Logger.getLogger(getClass().getName());
    
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}	
	
	@GetMapping("/register")
	public String showRegistrationPage(Model theModel) {
		RegisteredUser user = new RegisteredUser();
		theModel.addAttribute("user", new RegisteredUser());
		return "register";
	}

	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(
				@ModelAttribute("user") RegisteredUser user,
				BindingResult theBindingResult, 
				Model theModel) {
		
		String userName = user.getUserName();
        if (userService.findByUserName(userName) != null){
        	theModel.addAttribute("user", new RegisteredUser());
			theModel.addAttribute("regError", "Username already exists.");
        	return "register";
        }
        

        userService.save(user);
        logger.info("Successfully created user: " + userName);
        
        return "registration-confirm";
	}
}
