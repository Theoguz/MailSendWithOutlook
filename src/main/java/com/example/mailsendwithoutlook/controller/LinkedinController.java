package com.example.mailsendwithoutlook.controller;

import com.example.mailsendwithoutlook.model.LinkedinImageRequest;
import com.example.mailsendwithoutlook.model.LinkedinRequest;
import com.example.mailsendwithoutlook.service.LinkedInService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/linkedin")
public class LinkedinController {

    private final LinkedInService linkedinService;

    public LinkedinController(LinkedInService linkedinService) {
        this.linkedinService = linkedinService;
    }

    @PostMapping("/text-upload")
    public ResponseEntity<String> uploadText(@RequestBody LinkedinRequest linkedinRequest) {

        try {
            // Implement text upload to LinkedIn
            linkedinService.postToLinkedIn(linkedinRequest.getText());
            return ResponseEntity.ok("Text uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading text: " + e.getMessage());
        }

    }

    @PostMapping("/image-upload")
    public ResponseEntity<String> uploadImage(@RequestBody LinkedinImageRequest linkedinImageRequest) {

        try {
            // Implement image upload to LinkedIn
            linkedinService.postToLinkedInImage(linkedinImageRequest.getPath());

            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }

    }

}
