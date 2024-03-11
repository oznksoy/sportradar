package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>Package encapsulated data entity to store scoreboard match details in correlation to {@link TeamPair}.</p>
 * <br>
 * <p>Match details mainly carries {@link Details#startTime} that signals initial match start time. Score details are also stored in this entity.</p>
 *
 * @author Ozan Aksoy
 */
class Details implements Serializable {

    private LocalDateTime startTime;

    private Score score;

    /**
     * Scoreboard match details in correlation to {@link TeamPair}.
     */
    public Details() {
    }

    /**
     * Deep copy enabling cloning constructor.
     *
     * @param details Match details to be cloned.
     */
    public Details(Details details) {
        this.startTime = details.getStartTime();
        this.score = new Score(details.getScore());
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
        return Objects.equals(startTime, details.startTime) && Objects.equals(score, details.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, score);
    }

    @Override
    public String toString() {
        return "Details{" + "matchTime=" + startTime + ", score=" + score + '}';
    }

}
