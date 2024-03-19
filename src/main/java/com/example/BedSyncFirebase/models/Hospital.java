package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

@Data
public class Hospital {
    @DocumentId
    private String id;
    private String name;
    private String location;
    private int capacity;
    // Other fields as needed
}
