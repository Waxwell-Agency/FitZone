package com.models;

import java.util.List;

public class Programme {

    private String id;
    private String code;
    private String title;
    private String description;
    private String coach;
    private String session;
    private String imageUrl;
    private List<String> annonces;

    public Programme() {
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoach() {
        return coach;
    }

    public String getSession() {
        return session;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getAnnonces() {
        return annonces;
    }
}