package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

import java.util.List;

@Data
public class Hospital {
    @DocumentId
    private String id;
    private String name;
    private String location;
    private int totalBeds;
    private int availableBeds;
    private int currentOccupancy;

    public Hospital(){

    }

}
