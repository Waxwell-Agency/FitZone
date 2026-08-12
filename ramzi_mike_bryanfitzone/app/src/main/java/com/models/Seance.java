package com.models;

public class Seance {

    private String id;
    private String programId;
    private String title;
    private String description;
    private String dueDate;
    private String instructions;
    private String status;
    private Integer grade;
    private String comment;
    private int totalPoints;
    private String type;

    public Seance() {
    }

    public String getId() {
        return id;
    }

    public String getProgramId() {
        return programId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getStatus() {
        return status;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getComment() {
        return comment;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getType() {
        return type;
    }
}