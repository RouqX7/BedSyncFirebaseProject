package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {

    List<User> findAll() throws ExecutionException, InterruptedException;

    Optional<User> findById(String uid) throws ExecutionException, InterruptedException;

    User save(User user) throws ExecutionException, InterruptedException;

    void deleteById(String id) throws ExecutionException, InterruptedException;

    // Add other methods as needed
}
