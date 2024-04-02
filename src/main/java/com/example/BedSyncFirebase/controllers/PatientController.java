package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/create-patient")
    public ResponseEntity<Patient> registerNewPatient(@PathVariable String hospitalId,@RequestBody Patient patient) {
        try {
            Patient registeredPatient = patientService.registerPatient(hospitalId,patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredPatient);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        try {
            return patientService.getPatientById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        try {
            return ResponseEntity.ok(patientService.save(updatedPatient));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/inNeedOfBed")
    public List<Patient> getAllAvailableBeds() throws ExecutionException, InterruptedException {
        return patientService.findByIsInNeedOfBed();
    }

    @PostMapping("/{patientId}/{bedId}/{wardId}/{hospitalId}/assign-bed")
    public ResponseEntity<?> assignPatientToBed(@PathVariable String patientId, @PathVariable String bedId,@PathVariable String wardId, @PathVariable String hospitalId)  {
        try {
            patientService.assignPatientToBed (patientId, bedId,wardId,hospitalId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning patient to bed: " + e.getMessage());
        }
    }

    @PostMapping("/{patientId}/{wardId}discharge") //I need to check to remove the BedId
    public ResponseEntity<?> dischargePatient(@PathVariable String patientId, @PathVariable String wardId) {
        try {
            patientService.dischargePatient(patientId,wardId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error discharging patient: " + e.getMessage());
        }
    }

}
