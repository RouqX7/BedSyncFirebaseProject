package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;


@Data
public class Bed {

    @DocumentId
    private String id;
    private String wardId;
    private String hospitalId;
    private boolean isAvailable = true;
    private String bedNumber;
    private String bedType;
    private String patientId;
    private boolean isClean = true;
    private ZonedDateTime admissionDate;
    private ZonedDateTime dischargeDate;
    private LocalDateTime timestamp;

    public Bed(){

    }
}
