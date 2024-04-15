package com.example.BedSyncFirebase.services;


import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Hospital;
import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.BedRepository;
import com.example.BedSyncFirebase.repos.HospitalRepository;
import com.example.BedSyncFirebase.repos.PatientRepository;
import com.example.BedSyncFirebase.repos.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
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

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalService hospitalService;

    public Patient registerPatient(String hospitalId,Patient patient) throws ExecutionException, InterruptedException {
        patient.setHospitalId(hospitalId);
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

    public void updatePatient(Patient patient) throws ExecutionException,InterruptedException{
        patientRepository.updatePatient(patient);
    }

    public void deletePatient(String id) throws ExecutionException, InterruptedException {
        patientRepository.deleteById(id);
    }

    public List<Patient> findByIsInNeedOfBed() throws ExecutionException,InterruptedException{
        return patientRepository.findByIsInNeedOfBed(true);
    }

    public void assignPatientToBed(String patientId,String hospitalId, String bedId, String wardId) throws ExecutionException, InterruptedException {
        // Get patient by ID
        Patient patient = getPatientById(patientId).orElseThrow(() -> new NoSuchElementException("Patient not found"));


        // Check if patient is already admitted
        if (patient.getAdmitted()) {
            throw new IllegalStateException("Patient is already admitted and cannot be assigned to another bed.");
        }

        // Get bed by ID
        Bed bed = bedService.getBedById(bedId);
        // Check if bed is dirty
        if (!bed.isClean()) {
            throw new IllegalStateException("Bed is dirty and cannot be assigned to a patient.");
        }
        // Get ward by ID
        Ward ward = wardService.getWardById(wardId).orElseThrow(() -> new NoSuchElementException("Ward not found"));
        Hospital hospital = hospitalService.getHospitalById(hospitalId).orElseThrow(() -> new NoSuchElementException("Hospital not found"));
        // Update patient's bed assignment and admission date
        patient.setBedId(bedId);
        patient.setAdmissionDate( ZonedDateTime.now(ZoneId.of("Europe/Dublin")));
        patient.setAdmitted(true);
        patient.setInNeedOfBed(false);
        updatePatient(patient);

        // Update bed state and availability
        bed.setPatientId(patientId);
        bed.setAvailable(false);
        bed.setInUse(true);
        bedRepository.updateBed(bed);

        hospital.setAvailableBeds(hospital.getAvailableBeds() - 1);
        hospital.setCurrentOccupancy(hospital.getCurrentOccupancy() - 1);
        hospitalRepository.updateHospital(hospital);

        // Update available beds count in the ward
        ward.setAvailableBeds(ward.getAvailableBeds() - 1);
        ward.setCurrentOccupancy(ward.getCurrentOccupancy() + 1);
        wardRepository.updateWard(ward);
    }






    public void dischargePatient(String patientId, String wardId) throws ExecutionException, InterruptedException {
        // Retrieve patient by ID
        Patient patient = getPatientById(patientId).orElseThrow(() -> new NoSuchElementException("Patient not found"));

        // Verify patient-bed association
        String bedId = patient.getBedId();
        Bed bed = bedService.getBedById(bedId);
        Ward ward = wardService.getWardById(wardId).orElseThrow(() -> new NoSuchElementException("Ward not found"));

        if (!bed.getPatientId().equals(patientId)) {
            throw new IllegalStateException("Selected patient does not match the patient assigned to the bed.");
        }

        // Perform discharge action
        // Update patient's discharge date and admitted flag
        patient.setDischargeDate( ZonedDateTime.now(ZoneId.of("Europe/Dublin")));
        patient.setAdmitted(false);
        patient.setInNeedOfBed(true);
        updatePatient(patient);

        // Update bed state and availability
        bed.setClean(false);
        bed.setAvailable(true);
        bed.setPatientId(null);
        bed.setInUse(false);
        bedRepository.updateBed(bed);

        // Update the ward
        ward.setAvailableBeds(ward.getAvailableBeds() + 1);
        ward.setCurrentOccupancy(ward.getCurrentOccupancy() - 1);
        wardRepository.updateWard(ward);
    }



}