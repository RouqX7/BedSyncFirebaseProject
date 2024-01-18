package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/wards")
public class WardController {

    @Autowired
    private WardService wardService;

    @GetMapping
    public ResponseEntity<?> getAllWards() {
        try {
            List<Ward> wards = wardService.getAllWards();
            return ResponseEntity.ok(wards);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error fetching wards: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWardById(@PathVariable String id) {
        try {
            return wardService.getWardById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error fetching ward: " + e.getMessage());
        }
    }

    @PostMapping("/create-ward")
    public ResponseEntity<?> createWard(@RequestBody Ward ward) {
        try {
            return ResponseEntity.ok(wardService.saveOrUpdateWard(ward));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error creating ward: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWard(@PathVariable String id, @RequestBody Ward ward) {
        try {
            return wardService.getWardById(id)
                    .map(existingWard -> {
                        ward.setUid(id);
                        try {
                            return ResponseEntity.ok(wardService.saveOrUpdateWard(ward));
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error updating ward: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWard(@PathVariable String uid) {
        try {
            wardService.deleteWard(uid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting ward: " + e.getMessage());
        }
    }

    // Other methods...

}
