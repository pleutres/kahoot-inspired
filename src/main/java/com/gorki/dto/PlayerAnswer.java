package com.gorki.dto;

public class PlayerAnswer {

    private Long playerId;

    private String answerText;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public String toString() {
        return "PlayerAnswer{" +
                "playerId=" + playerId +
                ", answerText='" + answerText + '\'' +
                '}';
    }
}
