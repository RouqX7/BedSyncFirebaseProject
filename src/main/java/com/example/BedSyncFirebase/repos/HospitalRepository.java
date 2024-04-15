package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.Hospital;
import com.example.BedSyncFirebase.models.Ward;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class HospitalRepository {

    @Autowired
    private Firestore firestore;

    public List<Hospital> findAll() throws ExecutionException, InterruptedException {
        CollectionReference hospitals = firestore.collection("hospitals");
        List<Hospital> hospitalList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = hospitals.get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            Hospital hospital = documentSnapshot.toObject(Hospital.class);
            hospitalList.add(hospital);
        }

        return hospitalList;
    }

    public Optional<Hospital> findById(String id) {
        DocumentReference docRef = firestore.collection("hospitals").document(id);
        try {
            ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
            DocumentSnapshot snapshot = documentSnapshot.get();

            if (snapshot.exists()) {
                Hospital hospital = snapshot.toObject(Hospital.class);
                return Optional.ofNullable(hospital);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Properly handle exceptions, such as logging or throwing a custom exception
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Hospital save(Hospital hospital) throws ExecutionException, InterruptedException {
        String newId =  UUID.randomUUID().toString();
        hospital.setId(newId);
        ApiFuture<WriteResult> future = firestore.collection("hospitals")
                .document(newId)
                .set(hospital);
        return hospital;
    }

    public Hospital updateHospital(Hospital hospital) throws ExecutionException, InterruptedException {
        DocumentReference hospitalRef = firestore.collection("hospitals").document(hospital.getId());
        ApiFuture<WriteResult> updateResult = hospitalRef.set(hospital);
        return hospital;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        CollectionReference hospitals = firestore.collection("hospitals");
        ApiFuture<QuerySnapshot> querySnapshot = hospitals.whereEqualTo("id", id).get();

        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            documentSnapshot.getReference().delete();
        }
    }
}
