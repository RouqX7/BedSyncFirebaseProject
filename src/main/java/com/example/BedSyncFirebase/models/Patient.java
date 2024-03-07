package com.example.BedSyncFirebase.models;


import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Patient {
    @DocumentId
    private String id;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private String contactInformation;
    private String medicalHistory;
    private String bedId;
    private Boolean admitted;
    private boolean inNeedOfBed =true;

    public Patient() {
        // Default constructor is needed for Firestore deserialization
    }


}


