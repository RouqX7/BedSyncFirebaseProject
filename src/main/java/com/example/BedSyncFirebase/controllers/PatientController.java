package com.example.BedSyncFirebase.controllers;

import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<Patient> registerNewPatient(@RequestBody Patient patient) {
        try {
            Patient registeredPatient = patientService.registerPatient(patient);
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
            return ResponseEntity.ok(patientService.saveOrUpdatePatient(updatedPatient));
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

    @PostMapping("/{uid}/assign/{bedId}")
    public ResponseEntity<Patient> assignPatientToBed(@PathVariable String id, @PathVariable String bedId) {
        try {
            Patient patient = patientService.assignPatientToBed(id, bedId);
            return ResponseEntity.ok(patient);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{uid}/unassign")
    public ResponseEntity<Patient> unAssignPatientFromBed(@PathVariable String id) {
        try {
            Patient patient = patientService.unassignPatientFromBed(id);
            return ResponseEntity.ok(patient);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
