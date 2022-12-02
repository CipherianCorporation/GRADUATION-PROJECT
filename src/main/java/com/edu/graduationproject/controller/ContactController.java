package com.edu.graduationproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);


    @GetMapping({"/account/contact"})
    public String contact(){
        return "account/contact";
    }
}
