package com.gorki.dto;

import java.util.ArrayList;

public class QuestionsList {

    private ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "QuestionsList{" +
                "questions=" + questions +
                '}';
    }
}
