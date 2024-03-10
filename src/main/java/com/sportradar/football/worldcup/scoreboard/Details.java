package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

class Details implements Serializable {

    private LocalDateTime matchTime;

    private Score score;

    public Details() {}

    public Details(Details details) {
        this.matchTime = details.getMatchTime();
        this.score = new Score(details.getScore());
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Details details)) return false;
        return Objects.equals(matchTime, details.matchTime) && Objects.equals(score, details.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchTime, score);
    }

    @Override
    public String toString() {
        return "Details{" +
                "matchTime=" + matchTime +
                ", score=" + score +
                '}';
    }

}
