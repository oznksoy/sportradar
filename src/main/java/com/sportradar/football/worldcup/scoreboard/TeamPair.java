package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.util.Objects;

public class TeamPair implements Serializable {

    private String homeTeam;

    private String awayTeam;

    public TeamPair() {
    }

    public TeamPair(TeamPair teamPair) {
        this.homeTeam = teamPair.getHomeTeam();
        this.awayTeam = teamPair.getAwayTeam();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamPair teamPair)) return false;
        return Objects.equals(homeTeam, teamPair.homeTeam) && Objects.equals(awayTeam, teamPair.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam);
    }

    @Override
    public String toString() {
        return "TeamPair{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                '}';
    }

}
