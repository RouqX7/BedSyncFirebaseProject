package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.User;
import com.example.BedSyncFirebase.services.UserService;
import com.example.BedSyncFirebase.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        try {
            Optional<User> optionalUser = userService.findUserByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        try {
            Optional<User> user = userService.getUserByUid(uid);
            return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/api/users/hospitalIdByEmail")
    public ResponseEntity<String> getHospitalIdByEmail(@RequestParam String email) {
        try {
            String hospitalId = userService.getHospitalIdByEmail(email);
            if (hospitalId != null) {
                return ResponseEntity.ok(hospitalId);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestBody User user) {
        try {
            // Validate user data
            if (!validationService.isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (!validationService.isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Invalid password format");
            }

            // Convert role to uppercase
            user.setRole(user.getRole().toUpperCase());

            // Continue with user registration
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Add other controller methods as needed
}

