package com.example.BedSyncFirebase.repos;

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

    public Optional<Ward> findByUid(String uid) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        ApiFuture<QuerySnapshot> querySnapshot = wards.whereEqualTo("uid", uid).get();

        if (!querySnapshot.get().isEmpty()) {
            QueryDocumentSnapshot documentSnapshot = querySnapshot.get().getDocuments().get(0);
            Ward ward = documentSnapshot.toObject(Ward.class);
            return Optional.of(ward);
        } else {
            return Optional.empty();
        }
    }

    public Ward save(Ward ward) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        ApiFuture<DocumentReference> result = wards.add(ward);

        // Optionally, you can set the generated ID back to the Ward object
        // ward.setId(result.get().getId());

        return ward;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        CollectionReference wards = firestore.collection("wards");
        ApiFuture<QuerySnapshot> querySnapshot = wards.whereEqualTo("id", id).get();

        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            documentSnapshot.getReference().delete();
        }
    }
}
