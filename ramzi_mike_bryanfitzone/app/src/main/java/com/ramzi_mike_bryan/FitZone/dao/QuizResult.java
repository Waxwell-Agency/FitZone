package com.ramzi_mike_bryan.FitZone.dao;

public class QuizResult {

    private final String quizId;
    private final int score;
    private final int total;
    private final String date;

    public QuizResult(String quizId, int score, int total, String date) {
        this.quizId = quizId;
        this.score = score;
        this.total = total;
        this.date = date;
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

    public String getDate() {
        return date;
    }
}
