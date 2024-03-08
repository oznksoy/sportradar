package com.sportradar.football.worldcup;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Score implements Serializable {

    private LocalDateTime matchDateTime;

    private Integer homeScore;

    private Integer awayScore;

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public void setMatchDateTime(LocalDateTime matchDateTime) {
        this.matchDateTime = matchDateTime;
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

}
