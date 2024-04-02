package com.example.BedSyncFirebase.repos;

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
public class WardRepository {

    @Autowired
    private Firestore firestore;

    public List<Ward> findAll() throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        List<Ward> wardList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = wards.get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            Ward ward = documentSnapshot.toObject(Ward.class);
            wardList.add(ward);
        }

        return wardList;
    }

    public Optional<Ward> findById(String id) {
        DocumentReference docRef = firestore.collection("wards").document(id);
        try {
            ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
            DocumentSnapshot snapshot = documentSnapshot.get();

            if (snapshot.exists()) {
                Ward ward = snapshot.toObject(Ward.class);
                return Optional.ofNullable(ward);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Properly handle exceptions, such as logging or throwing a custom exception
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public Ward save(Ward ward) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        ApiFuture<DocumentReference> result = wards.add(ward);
        return ward;
    }

    public Ward updateWard(Ward ward) throws ExecutionException, InterruptedException {
        DocumentReference wardRef = firestore.collection("wards").document(ward.getId());
        ApiFuture<WriteResult> updateResult = wardRef.set(ward);
        return ward;
    }


    public void deleteById(String id) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        ApiFuture<QuerySnapshot> querySnapshot = wards.whereEqualTo("id", id).get();

        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            documentSnapshot.getReference().delete();
        }
    }
    public List<Ward> findByHospitalId(String hospitalId) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        List<Ward> wardList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = wards.whereEqualTo("hospitalId", hospitalId).get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            Ward ward = documentSnapshot.toObject(Ward.class);
            wardList.add(ward);
        }

        return wardList;
    }
//    public List<Ward> findCurrentOccupancy(int currentOccupancy) throws ExecutionException, InterruptedException {
//        CollectionReference wards = firestore.collection("wards");
//        QuerySnapshot querySnapshot = wards.whereEqualTo("currentOccupancy", currentOccupancy).get().get();
//
//        List<Ward> wardList = new ArrayList<>();
//        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
//            Ward ward = document.toObject(Ward.class);
//            wardList.add(ward);
//        }
//
//        return wardList;
//    }


}
