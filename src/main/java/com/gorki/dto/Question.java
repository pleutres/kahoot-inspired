package com.gorki.dto;

import java.util.List;

public class Question {

    private String title;

    private int waitTime;

    private List<Answer> answers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isAnswerCorrect(String text) {
        for (Answer answer : answers) {
            if (answer.getText().equals(text)) {
                return answer.isCorrect();
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", waitTime=" + waitTime +
                ", answers=" + answers +
                '}';
    }
}
