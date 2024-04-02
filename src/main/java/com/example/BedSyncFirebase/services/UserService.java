package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.User;
import com.example.BedSyncFirebase.repos.UserRepository;
import com.example.BedSyncFirebase.repos.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private UserRepositoryImpl userRepository;

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email) throws ExecutionException, InterruptedException {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUid(String uid) throws ExecutionException, InterruptedException {
        return userRepository.findById(uid);
    }

    public String getHospitalIdByEmail(String email) throws ExecutionException,InterruptedException{
        return userRepository.getHospitalIdByEmail(email);
    }

    public User registerUser(User user) throws ExecutionException, InterruptedException {
        return userRepository.save(user);
    }

    public void deleteUserById(String id) throws ExecutionException, InterruptedException {
        userRepository.deleteById(id);
    }
}
