package com.vsb.sim0323.sokoban;

public class Score {
    int id;
    int score;
    boolean isCompleted;

    public Score(int id, int score, boolean isCompleted) {
        this.id = id;
        this.score = score;
        this.isCompleted = isCompleted;
    }

    public Score() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
