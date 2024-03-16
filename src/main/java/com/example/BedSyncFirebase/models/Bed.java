package com.example.BedSyncFirebase.models;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private String patientId;
    private boolean isClean = true;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private LocalDateTime timestamp;

    public Bed(){

    }
}
