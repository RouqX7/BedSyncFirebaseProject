package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Optional<User> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            User user = document.toObject(User.class);
            assert user != null;
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public User save(User user) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        ApiFuture<DocumentReference> result = users.add(user);

        // Optionally, you can set the generated ID back to the User object
        // user.setId(result.get().getId());

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
