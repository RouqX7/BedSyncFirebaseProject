package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;

    public List<Ward> getAllWards() throws ExecutionException, InterruptedException {
        return wardRepository.findAll();
    }

    public Optional<Ward> getWardById(String id) throws ExecutionException, InterruptedException {
        return wardRepository.findByUid(id);
    }

    public Ward saveOrUpdateWard(Ward ward) throws ExecutionException, InterruptedException {
        return wardRepository.save(ward);
    }

    public void deleteWard(String id) throws ExecutionException, InterruptedException {
        wardRepository.deleteById(id);
    }

    //Might add extra logic
    // Example of a method using custom logic:
    public List<Ward> getWardsByName(String name) throws ExecutionException, InterruptedException {
        List<Ward> allWards = wardRepository.findAll();
        return allWards.stream()
                .filter(ward -> ward.getName().equals(name))
                .collect(Collectors.toList());
    }
}
