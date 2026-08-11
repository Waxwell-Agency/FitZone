package com.models;

import java.util.List;

public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String telephone;
    private String photoUrl;
    private List<String> enrolledProgramIds;

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<String> getEnrolledProgramIds() {return enrolledProgramIds;}
}