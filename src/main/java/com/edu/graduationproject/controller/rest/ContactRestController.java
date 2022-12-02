package com.edu.graduationproject.controller.rest;

import com.edu.graduationproject.service.ContactService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
public class ContactRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportRestController.class);

    @Autowired
    ContactService contactService;

    @PostMapping("/rest/send_contact")
    public void sendContact(@RequestBody JsonNode contactData){
        contactService.sendContact(contactData);
    }
}
