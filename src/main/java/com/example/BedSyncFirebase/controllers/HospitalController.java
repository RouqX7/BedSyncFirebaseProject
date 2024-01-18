package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.services.BedService;
import com.example.BedSyncFirebase.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hospital/")
public class HospitalController {

    @Autowired
    private WardService wardService;

    @Autowired
    private BedService bedService;

    // Endpoint to fetch a specific ward
    @GetMapping("/wards/{wardId}")
    public ResponseEntity<Ward> getWard(@PathVariable String wardId) throws ExecutionException, InterruptedException {
        return wardService.getWardById(wardId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to fetch beds for a specific ward
    @GetMapping("/wards/{wardId}/beds")
    public ResponseEntity<List<Bed>> getBedsByWard(@PathVariable String wardId) throws ExecutionException, InterruptedException {
        List<Bed> beds = bedService.getBedsByWard(wardId);
        return ResponseEntity.ok(beds);
    }

    // Other endpoints...
}