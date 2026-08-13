package com.models;

public class Aliment {

    private String id;
    private String nom;
    private String icone;
    private String description;
    private int calories;
    private String moment;

    public Aliment() {
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getIcone() {
        return icone;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public String getMoment() {
        return moment;
    }
}
