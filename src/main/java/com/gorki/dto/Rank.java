package com.gorki.dto;

public class Rank {

    private Long playerId;

    private boolean correct;

    private String playerName;

    private String answer;

    public Rank(Long playerId, String playerName, String answer, boolean correct) {
        this.playerId = playerId;
        this.correct = correct;
        this.playerName = playerName;
        this.answer = answer;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rank)) return false;

        Rank rank = (Rank) o;

        return playerId != null ? playerId.equals(rank.playerId) : rank.playerId == null;
    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "playerId=" + playerId +
                ", correct=" + correct +
                ", playerName='" + playerName + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
