package com.healthcare.queuesystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Healthcare Queue Management System");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("description", "A comprehensive healthcare queue management system for hospitals and clinics");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("patients", "/api/patients");
        endpoints.put("doctors", "/api/doctors");
        endpoints.put("appointments", "/api/appointments");
        endpoints.put("queue", "/api/queue");
        endpoints.put("admin", "/api/admin");
        endpoints.put("health", "/actuator/health");
        
        response.put("endpoints", endpoints);
        response.put("message", "Welcome to Healthcare Queue Management System API");
        
        return response;
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "Healthcare Queue Management System");
        response.put("timestamp", java.time.Instant.now().toString());
        return response;
    }
}