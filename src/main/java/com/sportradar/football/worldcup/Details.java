package com.sportradar.football.worldcup;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Details implements Serializable {

    LocalDateTime matchTime;

    Score score;

    public Details(LocalDateTime matchTime) {
        this.matchTime = matchTime;
        this.score = new Score(0 , 0);
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

}
