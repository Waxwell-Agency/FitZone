package com.models;

import java.util.List;

public class Quiz {

    private String id;
    private String programId;
    private String title;
    private List<Question> questions;

    public Quiz() {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public static class Question {

        private String id;
        private String question;
        private List<String> options;
        private int correctOption;

        public Question() {
        }

        public String getId() {
            return id;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public int getCorrectOption() {
            return correctOption;
        }
    }
}
