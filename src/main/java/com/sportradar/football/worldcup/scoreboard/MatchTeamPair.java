package com.sportradar.football.worldcup.scoreboard;

import java.io.Serializable;
import java.util.Objects;

/**
 * Package encapsulated data entity to store  two teams in the match to the scoreboard.
 * This pair is to be used as key in correlation to {@link MatchDetails} mapping.
 *
 * @author Ozan Aksoy
 */
class MatchTeamPair implements Serializable {

    private String homeTeam;

    private String awayTeam;

    /**
     * Scoreboard match team pairs to be used as key in correlation to {@link MatchDetails}.
     */
    public MatchTeamPair() {
    }

    /**
     * Deep copy enabling cloning constructor.
     *
     * @param teamPair Two teams of the match
     */
    public MatchTeamPair(MatchTeamPair teamPair) {
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
        if (!(o instanceof MatchTeamPair teamPair)) return false;
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
