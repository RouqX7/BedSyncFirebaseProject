package com.example.BedSyncFirebase.models;


import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;


import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class Patient {
    @DocumentId
    private String id;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private ZonedDateTime admissionDate;
    private ZonedDateTime dischargeDate;
    private String contactInformation;
    private String medicalHistory;
    private String bedId;
    protected String hospitalId;
    private Boolean admitted = false;
    private boolean inNeedOfBed =true;
    private String priority;
    private String issue;
    private Duration stayDuration;

    public Patient() {
        // Default constructor is needed for Firestore deserialization
    }

    public void calculateStayDuration() {
        if (admissionDate != null && dischargeDate != null) {
            stayDuration = Duration.between(admissionDate, dischargeDate);
        } else if (admissionDate != null) {
            stayDuration = Duration.between(admissionDate, ZonedDateTime.now());
        } else {
            stayDuration = null; // Handle case where admission date is missing
        }
    }



}


