package com.example.BedSyncFirebase.services;

import com.example.BedSyncFirebase.models.Hospital;
import com.example.BedSyncFirebase.repos.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals() throws ExecutionException, InterruptedException {
        return hospitalRepository.findAll();
    }

    public Optional<Hospital> getHospitalById(String id) throws ExecutionException, InterruptedException {
        return hospitalRepository.findById(id);
    }

    public Hospital createHospital(Hospital hospital) throws ExecutionException, InterruptedException {
        return hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(Hospital hospital) throws ExecutionException, InterruptedException {
        return hospitalRepository.updateHospital(hospital);
    }

    public void deleteHospital(String id) throws ExecutionException, InterruptedException {
        hospitalRepository.deleteById(id);
    }
}
