package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.BedRepository;
import com.example.BedSyncFirebase.repos.WardRepository;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class BedService {


    @Autowired
    private  BedRepository bedRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private WardService wardService;

    @Autowired
    Firestore firestore;

    public List<Bed> getAllBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findAll();
    }

    public Bed Bed(Bed bed) throws ExecutionException, InterruptedException{
        return bedRepository.updateBed(bed);
    }

    public Bed getBedById(String id) throws ExecutionException, InterruptedException {
        return bedRepository.findById(id)
                .orElse(null);
    }

    public Bed createBed(Bed bed) throws ExecutionException, InterruptedException {
        return bedRepository.save(bed);
    }
    public void deleteBed(String id, String wardId) throws ExecutionException, InterruptedException {
        // Retrieve the bed to get the associated wardId and availability
        Optional<Bed> optionalBed = bedRepository.findById(id);
        if (optionalBed.isPresent()) {
            Bed bed = optionalBed.get();

            // Check if the wardId matches the associated wardId of the bed
            if (!bed.getWardId().equals(wardId)) {
                throw new IllegalArgumentException("Mismatched wardId");
            }

            boolean isAvailable = bed.isAvailable(); // Assuming you have a method to check availability

            // Delete the bed from the database
            bedRepository.deleteById(id);

            // Update the totalBeds field in the associated Ward
            decrementTotalBeds(wardId);

            // Update the availableBeds field in the associated Ward if the bed was available
            if (isAvailable) {
                decrementAvailableBeds(wardId);
            }
        } else {
            throw new RuntimeException("Bed not found with ID: " + id);
        }
    }



    private void decrementTotalBeds(String wardId) throws ExecutionException, InterruptedException {
        Optional<Ward> optionalWard = wardRepository.findById(wardId);
        if (optionalWard.isPresent()) {
            Ward ward = optionalWard.get();
            int totalBeds = ward.getTotalBeds();
            if (totalBeds > 0) {
                ward.setTotalBeds(totalBeds - 1); // Decrement totalBeds by 1
                wardRepository.updateWard(ward); // Save the updated ward in the database
            }
        } else {
            throw new RuntimeException("Ward not found with ID: " + wardId);
        }
    }

    private void decrementAvailableBeds(String wardId) throws ExecutionException, InterruptedException {
        Optional<Ward> optionalWard = wardRepository.findById(wardId);
        if (optionalWard.isPresent()) {
            Ward ward = optionalWard.get();
            int availableBeds = ward.getAvailableBeds();
            if (availableBeds > 0) {
                ward.setAvailableBeds(availableBeds - 1); // Decrement availableBeds by 1
                wardRepository.updateWard(ward); // Save the updated ward in the database
            }
        } else {
            throw new RuntimeException("Ward not found with ID: " + wardId);
        }
    }

    public List<Bed> getBedsByWard(String wardId) throws ExecutionException, InterruptedException {
        return bedRepository.findByWardId(wardId);
    }
    public List<Bed> getAllAvailableBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findByIsAvailable(true);
    }

    public List<Bed> getDirtyBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findIfClean(false);
    }

    public List<Bed> getCleanBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findIfClean(true);
    }


    public List<Bed> findAvailableBedsByWardId(String wardId) throws ExecutionException, InterruptedException {
        return bedRepository.findAvailableBedsByWardId(wardId);
    }

//    public Duration getOccupancyTimeForBed(String bedId) throws ExecutionException, InterruptedException {
//        Optional<Bed> optionalBed = Optional.ofNullable(getBedById(bedId));
//        if (optionalBed.isPresent()) {
//            Bed bed = optionalBed.get();
//            return bed.calculateOccupancyTime();
//        } else {
//            throw new RuntimeException("Bed not found with ID: " + bedId);
//        }
//    }

    public void markAsClean(String bedId) throws ExecutionException, InterruptedException {
        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new IllegalArgumentException("Bed not found with ID: " + bedId));
        bed.setClean(true);
        bed.setAvailable(true);
        bedRepository.updateBed(bed);
    }

    public void markAsDirty(String bedId) throws ExecutionException, InterruptedException {
        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new IllegalArgumentException("Bed not found with ID: " + bedId));
        bed.setClean(false);
        bed.setAvailable(false);
        bedRepository.updateBed(bed);
    }
    public List<Bed> getBedsByTimestampRange(LocalDateTime startTimestamp, LocalDateTime endTimestamp) throws ExecutionException, InterruptedException {
        return bedRepository.findByTimestampBetween(startTimestamp, endTimestamp);
    }

}
