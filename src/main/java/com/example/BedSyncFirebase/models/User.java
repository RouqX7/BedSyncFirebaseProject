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

    private String firstName;

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
    private String profilePictureUrl;

    public User(){

    }


}
