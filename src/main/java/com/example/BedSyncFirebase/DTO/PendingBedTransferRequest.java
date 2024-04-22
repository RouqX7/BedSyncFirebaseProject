package com.example.BedSyncFirebase.DTO;

import lombok.Data;

@Data
public class PendingBedTransferRequest {
    private String bedId;
    private String requestingHospitalId;
    private String patientId;
    private String requestingPatientId;
    private String transferReason;


    public PendingBedTransferRequest(){

    }

}