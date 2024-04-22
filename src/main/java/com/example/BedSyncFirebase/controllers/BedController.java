package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.DTO.PendingBedTransferRequest;
import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Hospital;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.services.BedService;
import com.example.BedSyncFirebase.services.HospitalService;
import com.example.BedSyncFirebase.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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

  @Autowired
  private HospitalService hospitalService;

    @GetMapping("/all")
    public List<Bed> getAllBeds() throws ExecutionException, InterruptedException {
        return bedService.getAllBeds();
    }

    @GetMapping("/availableBeds")
    public List<Bed> getAllAvailableBeds(@RequestParam(required = false) String hospitalId) throws ExecutionException, InterruptedException {
        if (hospitalId != null && !hospitalId.isEmpty()) {
            return bedService.getAllAvailableBedsByHospital(hospitalId);
        }
        return bedService.getAllAvailableBeds();
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

    @DeleteMapping("/wards/{hospitalId}/{wardId}/beds/{bedId}")
    public ResponseEntity<?> deleteBed(@PathVariable String hospitalId, @PathVariable String wardId, @PathVariable String bedId) {
        try {
            bedService.deleteBed(bedId, hospitalId, wardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting bed: " + e.getMessage());
        }
    }

    @PutMapping("/{bedId}")
    public ResponseEntity<?> updateBed(@PathVariable String bedId, @RequestBody Bed bed ) {
        try {
            Bed existingBed = bedService.getBedById(bedId);

            if (existingBed != null) {
                bed.setId(existingBed.getId()); // Set ID from existing bed
                try {
                    return ResponseEntity.ok(bedService.updateBed(bed));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error updating ward: " + e.getMessage());
        }
    }
    @GetMapping("/available")
    public List<Bed> getAllAvailableBeds() throws ExecutionException, InterruptedException {
        return bedService.getAllAvailableBeds();
    }

    @GetMapping("/dirty")
    public List<Bed> getDirtyBeds() throws ExecutionException, InterruptedException {
        return bedService.getDirtyBeds();
    }

    @GetMapping("/clean")
    public List<Bed> getCleanBeds() throws ExecutionException, InterruptedException {
        return bedService.getCleanBeds();
    }



    @GetMapping("/available/{wardId}")
    public List<Bed> getAvailableBedsByWardId(@PathVariable String wardId) throws ExecutionException, InterruptedException {
        return bedService.findAvailableBedsByWardId(wardId);
    }

    @GetMapping("/wards/{wardId}/bedsInWard")
    public ResponseEntity<?> getBedsByWard(@PathVariable String wardId) {
        try {
            List<Bed> beds = bedService.getBedsByWard(wardId);
            return ResponseEntity.ok(beds);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/wards/{hospitalId}/{wardId}/create-bed")
    public ResponseEntity<?> createBedForWard(@PathVariable String hospitalId, @PathVariable String wardId, @RequestBody Bed bed) {
        try {

            Bed createdBed = bedService.createBed(hospitalId,wardId, bed);

            return ResponseEntity.ok(createdBed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getBedsByHospitalId/{hospitalId}")
    public ResponseEntity<List<Bed>> getBedsByHospitalId(@PathVariable String hospitalId) {
        try {
            List<Bed> beds = bedService.getBedsByHospitalId(hospitalId);
            return ResponseEntity.ok(beds);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).build();
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

    @PutMapping("/{bedId}/markClean")
    public ResponseEntity<?> markBedAsClean(@PathVariable String bedId) throws ExecutionException, InterruptedException {
        bedService.markAsClean(bedId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{bedId}/markDirty")
    public ResponseEntity<?> markBedAsDirty(@PathVariable String bedId) throws ExecutionException, InterruptedException {
        bedService.markAsDirty(bedId);

        return ResponseEntity.ok().build();
    }


    // Endpoint for requesting bed transfer
    @PostMapping("/{bedId}/requestBedTransfer")
    public ResponseEntity<String> requestBedTransfer(@PathVariable String bedId,
                                                     @RequestBody PendingBedTransferRequest request) {
        try {
            bedService.requestBedTransfer(bedId, request.getRequestingHospitalId(),request.getRequestingPatientId(), request.getTransferReason());
            return ResponseEntity.ok("Bed transfer requested successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bed not found with ID: " + bedId);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Bed is currently in use and cannot be transferred.");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error requesting bed transfer: " + e.getMessage());
        }
    }



    // Endpoint for responding to bed transfer request
    @PostMapping("/{bedId}/transfer/response")
    public ResponseEntity<String> respondToBedTransferRequest(@PathVariable String bedId, @RequestParam boolean isAccepted, @RequestParam String responseReason) throws ExecutionException, InterruptedException {
        bedService.respondToBedTransferRequest(bedId, isAccepted, responseReason);
        return ResponseEntity.ok("Bed transfer response recorded successfully.");
    }

    // Endpoint for performing bed transfer
    @PostMapping("/{bedId}/transfer/perform")
    public ResponseEntity<String> performBedTransfer(@PathVariable String bedId) throws ExecutionException, InterruptedException {
        bedService.performBedTransfer(bedId);
        return ResponseEntity.ok("Bed transfer performed successfully.");
    }

    @GetMapping("/pending-transfer-requests")
    public ResponseEntity<List<PendingBedTransferRequest>> getPendingBedTransferRequests() {
        try {
            List<PendingBedTransferRequest> pendingRequests = bedService.getPendingBedTransferRequests();
            return ResponseEntity.ok(pendingRequests);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); 
        }
    }





    // Other methods as needed
}
