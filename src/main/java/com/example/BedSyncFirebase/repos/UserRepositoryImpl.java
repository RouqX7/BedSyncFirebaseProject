package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.User;
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
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private Firestore firestore;

    @Override
    public List<User> findAll() throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        List<User> userList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = users.get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            User user = documentSnapshot.toObject(User.class);
            userList.add(user);
        }

        return userList;
    }

    @Override
    public Optional<User> findById(String id) {
        DocumentReference docRef = firestore.collection("users").document(id);
        try {
            ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
            DocumentSnapshot snapshot = documentSnapshot.get();

            if (snapshot.exists()) {
                User user = snapshot.toObject(User.class);
                return Optional.ofNullable(user);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Properly handle exceptions, such as logging or throwing a custom exception
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("email", email).limit(1); // Limiting query to retrieve only one document
        QuerySnapshot querySnapshot = query.get().get();

        if (!querySnapshot.isEmpty()) {
            QueryDocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
            User user = documentSnapshot.toObject(User.class);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getHospitalIdByEmail(String email) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("email", email).limit(1); // Limiting query to retrieve only one document
        QuerySnapshot querySnapshot = query.get().get();

        if (!querySnapshot.isEmpty()) {
            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
            User user = documentSnapshot.toObject(User.class);
            return user.getHospitalId(); // Assuming there's a getter method for hospitalId in your User class
        } else {
            return null;
        }
    }




    @Override
    public User save(User user) throws ExecutionException, InterruptedException {
        String newId =  UUID.randomUUID().toString();
        user.setUserId(newId);
        ApiFuture<WriteResult> future = firestore.collection("users")
                .document(newId)
                .set(user);

        return user;
    }

    @Override
    public void deleteById(String id) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        ApiFuture<QuerySnapshot> querySnapshot = users.whereEqualTo("id", id).get();

        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            documentSnapshot.getReference().delete();
        }
    }
}
