package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.DTO.PendingBedTransferRequest;
import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Hospital;
import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.BedRepository;
import com.example.BedSyncFirebase.repos.HospitalRepository;
import com.example.BedSyncFirebase.repos.PatientRepository;
import com.example.BedSyncFirebase.repos.WardRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class BedService {


    @Autowired
    private  BedRepository bedRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Lazy
    private PatientService patientService;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private WardService wardService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    Firestore firestore;

    @Autowired
    HospitalRepository hospitalRepository;

    public List<Bed> getAllBeds() throws ExecutionException, InterruptedException {
        return bedRepository.findAll();
    }

    public Bed updateBed(Bed bed) throws ExecutionException, InterruptedException{
        return bedRepository.updateBed(bed);
    }

    public Bed getBedById(String id) throws ExecutionException, InterruptedException {
        return bedRepository.findById(id)
                .orElse(null);
    }

    public Bed createBed(String hospitalId, String wardId, Bed bed) throws ExecutionException, InterruptedException {
        bed.setWardId(wardId);  // Associate the bed with the specified wardId
        bed.setHospitalId(hospitalId);

        try {
            Optional<Ward> optionalWard = wardService.getWardById(wardId);
            if (optionalWard.isPresent()) {
                Ward foundWard = optionalWard.get();  // Renamed to avoid shadowing
                foundWard.setTotalBeds(foundWard.getTotalBeds() + 1);
                foundWard.setAvailableBeds(foundWard.getAvailableBeds() + 1);
                wardService.updateWard(foundWard);
            } else {
                throw new RuntimeException("Ward not found with ID: " + wardId);
            }

            Optional<Hospital> optionalHospital = hospitalService.getHospitalById(hospitalId);
            if (optionalHospital.isPresent()) {
                Hospital foundHospital = optionalHospital.get();  // Renamed to avoid shadowing
                foundHospital.setTotalBeds(foundHospital.getTotalBeds() + 1);
                foundHospital.setAvailableBeds(foundHospital.getAvailableBeds() + 1);
                hospitalService.updateHospital(foundHospital);
            }

            return bedRepository.save(bed);
        }catch (Exception e) {
            System.out.println("Original exception: " + e.getClass().getName() + ": " + e.getMessage());
            if (e instanceof InvocationTargetException && e.getCause() != null) {
                System.out.println("Wrapped exception: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
            }
            throw e; // Rethrow the exception to be handled by the caller
        }
    }






    public void deleteBed(String id, String hospitalId, String wardId) throws ExecutionException, InterruptedException {
        // Retrieve the bed
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bed not found with ID: " + id));

        // Ensure the bed is associated with the correct ward
        if (!bed.getWardId().equals(wardId)) {
            throw new IllegalArgumentException("Mismatched wardId");
        }

        // Check if the bed is available
        if (!bed.isAvailable()) {
            throw new IllegalStateException("Bed is currently in use and cannot be deleted.");
        }

        // Retrieve the associated ward and hospital
        Ward ward = wardService.getWardById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found with ID: " + wardId));

        Hospital hospital = hospitalService.getHospitalById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + hospitalId));

        // Update counts
        ward.setTotalBeds(ward.getTotalBeds() - 1);
        ward.setAvailableBeds(ward.getAvailableBeds() - 1);
        wardService.updateWard(ward);

        hospital.setTotalBeds(hospital.getTotalBeds() - 1);
        hospital.setAvailableBeds(hospital.getAvailableBeds() - 1);
        hospitalService.updateHospital(hospital);

        // Delete the bed from the database
        bedRepository.deleteById(id);
    }


    public List<Bed> getBedsByWard(String wardId) throws ExecutionException, InterruptedException {
        return bedRepository.findByWardId(wardId);
    }

    public List<Bed> getBedsByHospitalId(String hospitalId) throws ExecutionException, InterruptedException {
        return bedRepository.findByHospitalId(hospitalId);
    }

    public List<Bed> getAllAvailableBedsByHospital(String hospitalId) throws ExecutionException, InterruptedException {
        return bedRepository.findByIsAvailableAndHospitalId(true, hospitalId);
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

    //Bed Transfer methods

    public void requestBedTransfer(String bedId, String requestingHospitalId, String requestingPatientId, String transferReason) throws ExecutionException, InterruptedException {
        Bed bed = getBedById(bedId);
        if (bed == null) {
            throw new IllegalArgumentException("Bed not found with ID: " + bedId);
        }
        if (!bed.isAvailable()) {
            throw new IllegalStateException("Bed is currently in use and cannot be transferred.");
        }

        bed.setTransferRequested(true);
        bed.setRequestingHospitalId(requestingHospitalId);
        bed.setRequestingPatientId(requestingPatientId);
        bed.setTransferReason(transferReason);
        bedRepository.updateBed(bed);
    }

    public void respondToBedTransferRequest(String bedId, boolean isAccepted, String responseReason) throws ExecutionException, InterruptedException {
        Bed bed = getBedById(bedId);
        if (bed == null) {
            throw new IllegalArgumentException("Bed not found with ID: " + bedId);
        }
        if (!bed.isTransferRequested()) {
            throw new IllegalStateException("No transfer request exists for this bed.");
        }

        bed.setTransferRequested(false);
        bed.setTransferStatus(isAccepted ? Bed.TransferStatus.ACCEPTED : Bed.TransferStatus.DECLINED);
        bed.setTransferResponseReason(responseReason);
        bedRepository.updateBed(bed);
    }

    public void performBedTransfer(String bedId) throws ExecutionException, InterruptedException {
        Bed bed = getBedById(bedId);
        if (bed == null) {
            throw new IllegalArgumentException("Bed not found with ID: " + bedId);
        }
        if (!bed.isTransferRequested() || bed.getTransferStatus() != Bed.TransferStatus.ACCEPTED) {
            throw new IllegalStateException("Transfer request must be accepted before performing the transfer.");
        }

        // Get the requesting patient's ID from the bed
        String requestingPatientId = bed.getRequestingPatientId();

        // Assign patient to bed using the patientService
        patientService.assignPatientToBed(requestingPatientId, bedId, bed.getWardId(), bed.getHospitalId());

        // Clear transfer-related fields
        bed.setTransferRequested(false);
        bed.setTransferReason(null);
        bed.setTransferStatus(null);
        bed.setTransferResponseReason(null);

        bedRepository.updateBed(bed);
    }


    public List<PendingBedTransferRequest> getPendingBedTransferRequests() throws ExecutionException, InterruptedException {
        List<PendingBedTransferRequest> pendingRequests = new ArrayList<>();

        List<Bed> beds = bedRepository.findByTransferRequestedTrue();

        for (Bed bed : beds) {
            PendingBedTransferRequest request = new PendingBedTransferRequest();
            request.setBedId(bed.getId());
            request.setRequestingHospitalId(bed.getRequestingHospitalId());
            request.setRequestingPatientId(bed.getRequestingPatientId());

            // Check if bed is admitted by retrieving the patient from the patient repository and checking the admitted flag
            Optional<Patient> patientOptional = patientRepository.findById(bed.getRequestingPatientId());
            request.setPatientId(patientOptional.filter(Patient::getAdmitted).map(Patient::getId).orElse(null));

            request.setTransferReason(bed.getTransferReason());
            pendingRequests.add(request);
        }

        return pendingRequests;
    }




}
