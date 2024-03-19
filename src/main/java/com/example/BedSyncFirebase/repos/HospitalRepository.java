package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.Hospital;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public Optional<Hospital> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("hospitals").document(id);
        ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();

        if (documentSnapshot.get().exists()) {
            Hospital hospital = documentSnapshot.get().toObject(Hospital.class);
            assert hospital != null;
            return Optional.of(hospital);
        } else {
            return Optional.empty();
        }
    }

    public Hospital save(Hospital hospital) throws ExecutionException, InterruptedException {
        CollectionReference hospitals = firestore.collection("hospitals");
        ApiFuture<DocumentReference> result = hospitals.add(hospital);
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
