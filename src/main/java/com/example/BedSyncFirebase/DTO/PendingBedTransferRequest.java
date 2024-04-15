package com.example.BedSyncFirebase.DTO;

import lombok.Data;

@Data
public class PendingBedTransferRequest {
    private String bedId;
    private String requestingHospitalId;
    private String patientId;
    private String transferReason;


    public PendingBedTransferRequest(){

    }

}