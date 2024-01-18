package com.example.BedSyncFirebase.services;


import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.repos.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient registerPatient(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() throws ExecutionException, InterruptedException {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(String id) throws ExecutionException, InterruptedException {
        return patientRepository.findById(id);
    }

    public Patient saveOrUpdatePatient(Patient updatedPatient) throws ExecutionException, InterruptedException {
        return patientRepository.save(updatedPatient);
    }

    public void deletePatient(String id) throws ExecutionException, InterruptedException {
        patientRepository.deleteById(id);
    }

    public Patient assignPatientToBed(String patientId, String bedId) throws ExecutionException, InterruptedException {
        Patient patient = getPatientById(patientId).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        patient.setBedId(bedId);
        return saveOrUpdatePatient(patient);
    }

    public Patient unassignPatientFromBed(String id) throws ExecutionException, InterruptedException {
        Patient patient = getPatientById(id).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        // Unassign the patient from the bed
        patient.setBedId(null);

        return saveOrUpdatePatient(patient);
    }
}