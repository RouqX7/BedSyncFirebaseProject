package com.example.BedSyncFirebase.models;

import com.example.BedSyncFirebase.states.BedState;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;


@Data
public class Bed {

    @DocumentId
    private String id;
    private String wardId;
    private boolean isAvailable = true;
    private String bedNumber;
    private String bedType;
    private BedState state;
    private String patientId;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private LocalDateTime timestamp;

    public Bed(){

    }

}
