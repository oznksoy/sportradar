package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Scoreboard aggregate match data representative entity.
 *
 * @author Ozan Aksoy
 */
public final class Match implements Serializable {

    private String homeTeam;

    private String awayTeam;

    private LocalDateTime matchTime;

    private Integer homeScore;

    private Integer awayScore;

    public Match() {
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
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
        if (!(o instanceof Match match)) return false;
        return Objects.equals(homeTeam, match.homeTeam)
                && Objects.equals(awayTeam, match.awayTeam)
                && Objects.equals(matchTime, match.matchTime)
                && Objects.equals(homeScore, match.homeScore)
                && Objects.equals(awayScore, match.awayScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, matchTime, homeScore, awayScore);
    }

    @Override
    public String toString() {
        return "Match{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", matchTime=" + matchTime +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }

}
