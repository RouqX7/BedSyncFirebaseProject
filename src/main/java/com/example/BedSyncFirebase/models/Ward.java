package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Ward {

    private String id;
    protected String name;
    protected String hospitalId;
    protected String location;
    protected String description;
    protected int currentOccupancy;
    //Add a limit that goes into the red if over occupied
    protected String responsibleDepartment;
    protected int totalBeds;
    protected int availableBeds;

    public Ward(){}


}
