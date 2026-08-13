package com.models;

public class QuizResult {

    private String quizId;
    private int score;
    private int total;

    public QuizResult() {
    }

    public String getQuizId() {
        return quizId;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}