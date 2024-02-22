package com.example.BedSyncFirebase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class User {

    @DocumentId
    private String id;

    @PropertyName("first_name")
    private String firstName;

    @PropertyName("last_name")
    private String lastName;

    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private String role;


    private String phoneNumber;
    private String securityQuestion;
    private String securityAnswer;
    private String alternateEmail;

    // Transient fields won't be stored in Firestore
//    private transient MultipartFile image;

    @PropertyName("profile_picture_url")
    private String profilePictureUrl;

    public User(String id, String firstName, String lastName, String email, String password, Date createdAt, Date updatedAt, String role, String phoneNumber, String securityQuestion, String securityAnswer, String alternateEmail, String profilePictureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.alternateEmail = alternateEmail;
        this.profilePictureUrl = profilePictureUrl;
    }
}
