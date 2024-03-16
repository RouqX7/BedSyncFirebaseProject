package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.Patient;
import com.example.BedSyncFirebase.models.Ward;
import com.example.BedSyncFirebase.repos.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;

    public List<Ward> getAllWards() throws ExecutionException, InterruptedException {
        return wardRepository.findAll();
    }

    public Optional<Ward> getWardById(String id) throws ExecutionException, InterruptedException {
        return wardRepository.findById(id);
    }

    public Ward createWard(Ward ward) throws ExecutionException, InterruptedException {
        return wardRepository.save(ward);
    }
    public Ward updateWard(Ward ward) throws ExecutionException, InterruptedException{
        return  wardRepository.updateWard(ward);
    }

    public void deleteWard(String id) throws ExecutionException, InterruptedException {
        wardRepository.deleteById(id);
    }




}
