package com.gorki.dto;

public class Answer {

    private String text;

    private boolean correct;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "text='" + text + '\'' +
                ", correct=" + correct +
                '}';
    }
}
