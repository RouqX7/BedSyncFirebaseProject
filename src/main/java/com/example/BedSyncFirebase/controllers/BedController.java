package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.services.BedService;
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

    @GetMapping("/all")
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

    @DeleteMapping("/delete/{id}/{wardId} ")
    public ResponseEntity<String> deleteBed(@PathVariable String id, @PathVariable String wardId) {
        try {
            Optional<Ward> optionalWard = wardService.getWardById(wardId);
            if (optionalWard.isPresent()) {
                // Ward exists, update the totalBeds field
                Ward ward = optionalWard.get();
                ward.setTotalBeds(ward.getTotalBeds() - 1); // Decrement totalBeds by 1
                wardService.updateWard(ward); // Update the ward

                // Check if the bed being deleted is available
                Optional<Bed> optionalBed = Optional.ofNullable(bedService.getBedById(id));
                if (optionalBed.isPresent() && optionalBed.get().isAvailable()) {
                    // Decrement the availableBeds field in the associated Ward if the bed is available
                    ward.setAvailableBeds(ward.getAvailableBeds() - 1);
                    wardService.updateWard(ward); // Update the ward again
                }
            } else {
                // Ward doesn't exist, handle the error
                throw new RuntimeException("Ward not found with ID: " + wardId);
            }
            bedService.deleteBed(id,wardId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PostMapping("/wards/{wardId}/create-bed")
    public ResponseEntity<?> createBedForWard(@PathVariable String wardId, @RequestBody Bed bed) {
        try {
            System.out.println("Ward ID received: " + wardId); // Add this line

            bed.setWardId(wardId);  // Associate the bed with the specified wardId
            Bed createdBed = bedService.createBed(bed);


            // Update the totalBeds field in the associated Ward
            Optional<Ward> optionalWard = wardService.getWardById(wardId);
            if (optionalWard.isPresent()) {
                // Ward exists, update the totalBeds field
                Ward ward = optionalWard.get();
                ward.setTotalBeds(ward.getTotalBeds() + 1); // Increment totalBeds by 1
                ward.setAvailableBeds(ward.getAvailableBeds() + 1);
                wardService.updateWard(ward); // Use the update method
            } else {
                // Ward doesn't exist, handle the error
                throw new RuntimeException("Ward not found with ID: " + wardId);
            }

            return ResponseEntity.ok(createdBed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




//    @GetMapping("/beds/{bedId}/occupancy")
//    public ResponseEntity<?> getOccupancyTimeForBed(@PathVariable String bedId) {
//        try {
//            Duration occupancyTime = bedService.getOccupancyTimeForBed(bedId);
//            return ResponseEntity.ok("Occupancy Time: " + occupancyTime.toHours() + " hours");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error calculating occupancy time: " + e.getMessage());
//        }
//    }


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





    // Other methods as needed
}
