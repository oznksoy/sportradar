package com.sportradar.football.worldcup;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Score implements Serializable {

    private Integer homeScore;

    private Integer awayScore;

    public Score(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

}
