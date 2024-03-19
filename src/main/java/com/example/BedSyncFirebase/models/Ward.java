package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public  class Ward {

    @DocumentId
    protected String id;
    protected String name; // Ward name
    protected String hospitalId;
    protected String location;
    protected int capacity;
    protected String description;
    protected String status;
    protected int currentOccupancy;
    protected String responsibleDepartment;
    protected int totalBeds;
    protected int availableBeds;
    protected LocalDateTime timestamp;

    public Ward(){

    }

}
