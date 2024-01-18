package com.example.BedSyncFirebase.models;


import com.google.cloud.firestore.annotation.DocumentId;

import java.util.Date;


public class Patient {

    @DocumentId
    private String uid;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String contactInformation;
    private String medicalHistory;
    private String bedId;

    public Patient(String uid, String firstName, String lastName, Date dateOfBirth, String contactInformation, String medicalHistory, String bedId) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactInformation = contactInformation;
        this.medicalHistory = medicalHistory;
        this.bedId = bedId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "uid='" + uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", contactInformation='" + contactInformation + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", bedId='" + bedId + '\'' +
                '}';
    }
}

