package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.services.BedService;
import com.example.BedSyncFirebase.services.WardService;
import com.example.BedSyncFirebase.states.BedState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/beds")
public class BedController {

  @Autowired
  private BedService bedService;
  @Autowired
  private WardService wardService;

    @GetMapping
    public List<Bed> getAllBeds() throws ExecutionException, InterruptedException {
        return bedService.getAllBeds();
    }

    @GetMapping("/{bedId}")
    public ResponseEntity<?> getBedById(@PathVariable String bedId) {
        try {
            Bed bed = bedService.getBedById(bedId);
            return ResponseEntity.ok(bed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving bed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable String id) throws ExecutionException, InterruptedException {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<?> updateBedState(@PathVariable String id, @RequestParam BedState state) {
        try {

            bedService.updateBedState(id, state, state);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/available")
    public List<Bed> getAllAvailableBeds() throws ExecutionException, InterruptedException {
        return bedService.getAllAvailableBeds();
    }

    @GetMapping("/available/{wardId}")
    public List<Bed> getAvailableBedsByWardId(@PathVariable String wardId) throws ExecutionException, InterruptedException {
        return bedService.findAvailableBedsByWardId(wardId);
    }


    @PatchMapping("/{wardId}/updateAvailableBeds")
    public ResponseEntity<?> updateWardAvailableBeds(@PathVariable String wardId, @RequestBody BedState newState) {
        try {
            bedService.updateAvailableBedsInWard(wardId, newState);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/wards/{wardId}/beds")
    public ResponseEntity<?> getBedsByWard(@PathVariable String wardId) {
        try {
            List<Bed> beds = bedService.getBedsByWard(wardId);
            return ResponseEntity.ok(beds);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/wards/{wardId}/create-bed")
    public ResponseEntity<?> createBedForWard(@PathVariable String wardId, @RequestBody Bed bed) {
        try {
            System.out.println("Ward ID received: " + wardId); // Add this line
            bed.setWardId(wardId);  // Associate the bed with the specified wardId
            Bed createdBed = bedService.createBed(bed);
            return ResponseEntity.ok(createdBed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/beds/{bedId}/updateStatus")
    public ResponseEntity<?> updateBedStatus(@PathVariable String bedId, @RequestParam boolean isAvailable) {
        try {
            bedService.updateBedStatus(bedId, isAvailable);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/beds/timestamp")
    public ResponseEntity<?> getBedsByTimestampRange(@RequestParam LocalDateTime startTimestamp,
                                                     @RequestParam LocalDateTime endTimestamp) {
        try {
            List<Bed> beds = bedService.getBedsByTimestampRange(startTimestamp, endTimestamp);
            return ResponseEntity.ok(beds);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    // Other methods as needed
}
