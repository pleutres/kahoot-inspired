package com.gorki.dto;

public class Score implements Comparable<Score>  {

    private Long playerId;

    private String playerName;

    private Integer score;

    public Score(Long playerId, String playerName, Integer score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "Score{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;

        Score rank = (Score) o;

        return playerId != null ? playerId.equals(rank.playerId) : rank.playerId == null;
    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }

    @Override
    public int compareTo(Score o) {
        return (o.getScore() < getScore()?-1:1);
    }
}
