package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.util.Objects;

class Score implements Serializable {

    private Integer homeScore;

    private Integer awayScore;

    public Score() {}

    public Score(Score score) {
        this.homeScore = score.getHomeScore();
        this.awayScore = score.getAwayScore();
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score score)) return false;
        return Objects.equals(homeScore, score.homeScore) && Objects.equals(awayScore, score.awayScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeScore, awayScore);
    }

    @Override
    public String toString() {
        return "Score{" +
                "homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }

}