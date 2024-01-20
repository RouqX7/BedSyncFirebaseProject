package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.BedRepository;
import com.example.BedSyncFirebase.repos.WardRepository;
import com.example.BedSyncFirebase.states.BedState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BedService {


    @Autowired
    private  BedRepository bedRepository;

    @Autowired
    private WardRepository wardRepository;

    public List<Bed> getAllBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findAll();
    }

    public Bed getBedById(String id) throws ExecutionException, InterruptedException {
        return bedRepository.findById(id)
                .orElse(null);
    }


    public Bed createBed(Bed bed) throws ExecutionException, InterruptedException {
        return bedRepository.save(bed);
    }

    public void deleteBed(String id) throws ExecutionException, InterruptedException {
        bedRepository.deleteById(id);
    }
    public void updateBedState(String bedId, BedState newState, BedState bedState) throws ExecutionException, InterruptedException {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("Bed not found with id: " + bedId));

        bed.setState(bedState);
        bedRepository.save(bed);

        updateAvailableBedsInWard(bed.getWardId(), (newState));
    }

    public void updateAvailableBedsInWard(String wardUid, BedState newState) throws ExecutionException, InterruptedException {
        Ward ward;
        try {
            ward = wardRepository.findById(wardUid)
                    .orElseThrow(() -> new RuntimeException("Ward not found with id: " + wardUid));

            int currentAvailableBeds = ward.getAvailableBeds();

            switch (newState.getStatus()) {
                case "OCCUPIED":
                case "DIRTY":
                    ward.setAvailableBeds(Math.max(0, currentAvailableBeds - 1));
                    break;
                case "AVAILABLE":
                case "CLEAN":
                    ward.setAvailableBeds(Math.min(ward.getTotalBeds(), currentAvailableBeds + 1));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid bed state: " + newState.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating ward available beds", e);
        }

        wardRepository.save(ward);
    }



    public List<Bed> getBedsByWard(String wardUid) throws ExecutionException, InterruptedException {
        return bedRepository.findByWardId(wardUid);
    }
    public void updateBedStatus(String bedUid, boolean isAvailable) throws ExecutionException, InterruptedException {
        Bed bed = bedRepository.findById(bedUid)
                .orElseThrow(() -> new RuntimeException("Bed not found with id: " + bedUid));

        bed.setAvailable(isAvailable);
        bedRepository.save(bed);
        Ward ward = wardRepository.findById(bed.getWardId())
                .orElseThrow(() -> new RuntimeException("Ward not found with id: " + bed.getWardId()));

        if (isAvailable) {
            ward.setAvailableBeds(Math.min(ward.getTotalBeds(), ward.getAvailableBeds() + 1));
        } else {
            ward.setAvailableBeds(ward.getAvailableBeds() - 1);
        }
        wardRepository.save(ward);
    }


    public List<Bed> getBedsByTimestampRange(LocalDateTime startTimestamp, LocalDateTime endTimestamp) throws ExecutionException, InterruptedException {
        return bedRepository.findByTimestampBetween(startTimestamp, endTimestamp);
    }

    // Other methods as needed
}
