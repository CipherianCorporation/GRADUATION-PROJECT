package com.edu.graduationproject.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContactService {
    public void sendContact(JsonNode contactData);
}
