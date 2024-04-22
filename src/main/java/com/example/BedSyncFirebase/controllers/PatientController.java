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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/create-patient/{hospitalId}")
    public ResponseEntity<Patient> registerNewPatient(@PathVariable String hospitalId, @RequestBody Patient patient) {
        try {
            Patient registeredPatient = patientService.registerPatient(hospitalId, patient);
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

    @PutMapping("/{patientId}")
    public ResponseEntity<?> updatePatient(@PathVariable String patientId, @RequestBody Patient patient) {
        try {
            Optional<Patient> existingPatientOptional = patientService.getPatientById(patientId);

            if (existingPatientOptional.isPresent()) {
                Patient existingPatient = existingPatientOptional.get();
                patient.setId(patientId);
                try {
                    patientService.updatePatient(patient);
                    return ResponseEntity.ok().build(); // Updated successfully, return 200 OK
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return ResponseEntity.notFound().build(); // Patient not found, return 404 Not Found
            }
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body("Error updating patient: " + e.getMessage()); // Error updating patient, return 400 Bad Request
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

    @PostMapping("/{patientId}/{wardId}/{hospitalId}/discharge")
    public ResponseEntity<?> dischargePatient(@PathVariable String patientId, @PathVariable String wardId, @PathVariable String hospitalId) {
        try {
            patientService.dischargePatient(patientId, wardId, hospitalId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error discharging patient: " + e.getMessage());
        }
    }

}
