package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class PatientRepository {

    @Autowired
    private Firestore firestore;

    public List<Patient> findAll() throws ExecutionException, InterruptedException {
        CollectionReference patients = firestore.collection("patients");
        List<Patient> patientList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = patients.get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            Patient patient = documentSnapshot.toObject(Patient.class);
            patientList.add(patient);
        }

        return patientList;
    }

    public Optional<Patient> findById(String id) {
        DocumentReference docRef = firestore.collection("patients").document(id);
        try {
            ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
            DocumentSnapshot snapshot = documentSnapshot.get();

            if (snapshot.exists()) {
                Patient patient = snapshot.toObject(Patient.class);
                return Optional.ofNullable(patient);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Properly handle exceptions, such as logging or throwing a custom exception
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Patient> findByIsInNeedOfBed(boolean inNeedOfBed) throws ExecutionException, InterruptedException {
        CollectionReference patients = firestore.collection("patients");
        QuerySnapshot querySnapshot = patients.whereEqualTo("inNeedOfBed", inNeedOfBed).get().get();

        List<Patient> PatientList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            Patient patient = document.toObject(Patient.class);
            PatientList.add(patient);
        }

        return PatientList;
    }


    public Patient save(Patient patient) throws ExecutionException, InterruptedException {
        CollectionReference patients = firestore.collection("patients");
        ApiFuture<DocumentReference> result = patients.add(patient);
        return patient;
    }

    public void updatePatient(Patient patient) throws ExecutionException,InterruptedException{
        DocumentReference patientRef = firestore.collection("patients").document(patient.getId());
        ApiFuture<WriteResult> updateResult = patientRef.set(patient);


    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        CollectionReference patients = firestore.collection("patients");
        ApiFuture<QuerySnapshot> querySnapshot = patients.whereEqualTo("id", id).get();

        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            documentSnapshot.getReference().delete();
        }
    }
}
