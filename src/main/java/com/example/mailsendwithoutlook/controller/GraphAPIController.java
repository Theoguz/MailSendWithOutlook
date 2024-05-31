package com.example.mailsendwithoutlook.controller;

import com.example.mailsendwithoutlook.model.EmailRequest;
import com.example.mailsendwithoutlook.model.EventRequest;
import com.example.mailsendwithoutlook.service.GraphAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
public class GraphAPIController {

    private final GraphAPIService graphAPIService;

    @Autowired
    public GraphAPIController(GraphAPIService graphAPIService) {
        this.graphAPIService = graphAPIService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            graphAPIService.sendEmail(emailRequest.getSubject(), emailRequest.getBody(), emailRequest.getRecipient());
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/send-event")
    public ResponseEntity<String> sendEvent(@RequestBody EventRequest eventRequest) {
        try {
            graphAPIService.sendEvent(eventRequest);
            return ResponseEntity.ok("Event created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating event: " + e.getMessage());
        }
    }
}