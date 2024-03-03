package com.example.BedSyncFirebase.services;


import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.BedRepository;
import com.example.BedSyncFirebase.repos.PatientRepository;
import com.example.BedSyncFirebase.repos.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private BedService bedService;

    @Autowired
    private WardService wardService;
    @Autowired
    private WardRepository wardRepository;

    public Patient registerPatient(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() throws ExecutionException, InterruptedException {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(String id) throws ExecutionException, InterruptedException {
        return patientRepository.findById(id);
    }

    public Patient save(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.save(patient);
    }

    public Patient update(Patient patient) throws ExecutionException,InterruptedException{
        return patientRepository.updatePatient(patient);
    }

    public void deletePatient(String id) throws ExecutionException, InterruptedException {
        patientRepository.deleteById(id);
    }

    public void assignPatientToBed(String patientId, String bedId, String wardId) throws ExecutionException, InterruptedException {
        // Get patient by ID
        Patient patient = getPatientById(patientId).orElseThrow(() -> new NoSuchElementException("Patient not found"));

        // Check if patient is already admitted
        if (patient.getAdmitted()) {
            throw new IllegalStateException("Patient is already admitted and cannot be assigned to another bed.");
        }

        // Get bed by ID
        Bed bed = bedService.getBedById(bedId);

        // Get ward by ID
        Ward ward = wardService.getWardById(wardId).orElseThrow(() -> new NoSuchElementException("Ward not found"));

        // Update patient's bed assignment and admission date
        patient.setBedId(bedId);
        patient.setAdmissionDate(LocalDateTime.now());
        patient.setAdmitted(true);
        update(patient);

        // Update bed state and availability
        bed.setPatientId(patientId);
        bed.setAvailable(false);
        bed.setAdmissionDate(LocalDateTime.now());
        bedRepository.updateBed(bed);

        // Update available beds count in the ward
        ward.setAvailableBeds(ward.getAvailableBeds() - 1);
        wardRepository.updateWard(ward);
    }




    public void dischargePatient(String patientId) throws ExecutionException, InterruptedException {
        // Retrieve patient by ID
        Patient patient = getPatientById(patientId).orElseThrow(() -> new NoSuchElementException("Patient not found"));

        // Verify patient-bed association
        String bedId = patient.getBedId();
        Bed bed = bedService.getBedById(bedId);
        if (!bed.getPatientId().equals(patientId)) {
            throw new IllegalStateException("Selected patient does not match the patient assigned to the bed.");
        }

        // Perform discharge action
        // Update patient's discharge date and admitted flag
        patient.setDischargeDate(LocalDateTime.now());
        patient.setAdmitted(false);
        update(patient);

        // Update bed state and availability
        bed.setAvailable(true);
        bed.setDischargeDate(LocalDateTime.now());
        bed.setPatientId(null);
        bedRepository.updateBed(bed);
    }





//    public Patient unassignPatientFromBed(String patientId) throws ExecutionException, InterruptedException {
//        Patient patient = getPatientById(patientId).orElse(null);
//
//        if (patient == null || patient.getBedId() == null) {
//            throw new NoSuchElementException("Patient not found or not assigned to a bed");
//        }
//
//        Bed bed = bedService.getBedById(patient.getBedId());
//
//        if (bed == null) {
//            throw new NoSuchElementException("Bed not found");
//        }
//
//        // Update patient's bed assignment
//        patient.setBedId(null);
//        saveOrUpdatePatient(patient);
//
//        // Update bed state and availability
//        bed.setPatientId(null);
//        bed.setState(new AvailableState()); // Set bed state to available
//        bed.setAvailable(true); // Set availability to true (available)
//        bedRepository.save(bed);
//
//        return patient;
//    }



}