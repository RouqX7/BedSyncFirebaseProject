package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;

import java.time.LocalDateTime;



public  class Ward {

    protected String id; // Unique identifier for the ward
    protected String name; // Ward name
    protected int capacity;
    protected String description;
    protected String status;
    protected int currentOccupancy;
    protected String responsibleDepartment;
    protected int totalBeds;
    protected int availableBeds;
    protected LocalDateTime timestamp;

    public Ward(){
        // Default constructor is needed for Firestore deserialization

    }


    public Ward(String id, String name, int capacity, String description, String status, int currentOccupancy, String responsibleDepartment, int totalBeds, int availableBeds, LocalDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.status = status;
        this.currentOccupancy = currentOccupancy;
        this.responsibleDepartment = responsibleDepartment;
        this.totalBeds = totalBeds;
        this.availableBeds = availableBeds;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public String getResponsibleDepartment() {
        return responsibleDepartment;
    }

    public void setResponsibleDepartment(String responsibleDepartment) {
        this.responsibleDepartment = responsibleDepartment;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(int totalBeds) {
        this.totalBeds = totalBeds;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(int availableBeds) {
        this.availableBeds = availableBeds;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "uid='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", currentOccupancy=" + currentOccupancy +
                ", responsibleDepartment='" + responsibleDepartment + '\'' +
                ", totalBeds=" + totalBeds +
                ", availableBeds=" + availableBeds +
                ", timestamp=" + timestamp +
                '}';
    }
}
