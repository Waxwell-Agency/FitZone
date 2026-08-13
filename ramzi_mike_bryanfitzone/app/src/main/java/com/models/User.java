package com.models;

import java.util.ArrayList;
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
    private List<String> completedSeanceIds;
    private List<QuizResult> quizResults;

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

    public List<String> getEnrolledProgramIds() {
        return enrolledProgramIds;
    }

    public List<String> getCompletedSeanceIds() {
        return completedSeanceIds;
    }

    public List<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setEnrolledProgramIds(List<String> enrolledProgramIds) {
        this.enrolledProgramIds = enrolledProgramIds;
    }

    public void setCompletedSeanceIds(List<String> completedSeanceIds) {
        this.completedSeanceIds = completedSeanceIds;
    }

    public void setQuizResults(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }
}