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

    public Optional<Patient> getPatientByUid(String uid) throws ExecutionException, InterruptedException {
        return patientRepository.findById(uid);
    }

    public Patient saveOrUpdatePatient(Patient updatedPatient) throws ExecutionException, InterruptedException {
        return patientRepository.save(updatedPatient);
    }

    public void deletePatient(String uid) throws ExecutionException, InterruptedException {
        patientRepository.deleteById(uid);
    }

    public Patient assignPatientToBed(String patientId, String bedId) throws ExecutionException, InterruptedException {
        Patient patient = getPatientByUid(patientId).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        patient.setBedId(bedId);
        return saveOrUpdatePatient(patient);
    }

    public Patient unassignPatientFromBed(String uid) throws ExecutionException, InterruptedException {
        Patient patient = getPatientByUid(uid).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + uid));

        // Unassign the patient from the bed
        patient.setBedId(null);

        return saveOrUpdatePatient(patient);
    }
}
