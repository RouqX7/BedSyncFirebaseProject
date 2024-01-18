package com.example.BedSyncFirebase.controllers;



import com.example.BedSyncFirebase.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/validation")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
public class ValidationController {
    private final ValidationService validationService;

    @Autowired
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/validate-email")
    public ResponseEntity<String> validateEmail(@Email @RequestParam String email) {
        return validationService.isValidEmail(email) ?
                ResponseEntity.ok("Email is valid") :
                ResponseEntity.badRequest().body("Invalid email format");
    }
    @PostMapping("/validate-password")
    public ResponseEntity<String> validatePassword(@RequestParam String password) {
        return validationService.isValidPassword(password) ?
                ResponseEntity.ok("Password is valid") :
                ResponseEntity.badRequest().body("Invalid password format");
    }
}