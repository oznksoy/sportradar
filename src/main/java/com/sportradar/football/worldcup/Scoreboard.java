package com.sportradar.football.worldcup;

import java.util.List;
import java.util.Set;

public class Scoreboard {

    /**
     * Initiates a match and registers it to the score board.
     */
    public void startMatch(String homeTeam, String awayTeam) {
        startMatch(new TeamPair(homeTeam, awayTeam));
    }

    public void startMatch(TeamPair teams) {

    }

    /**
     * Receives a pair of absolute scores: home team score and away team score, then updates the scoreboard.
     *
     * @param homeTeamScore
     * @param awayTeamScore
     */
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        //TODO: implement
    }

    /**
     * This removes a match from the scoreboard
     */
    public void finishMatch(String homeTeam, String awayTeam) {
        //TODO: implement
    }

    public List<Match> summarize() {
        //TODO: implement
        return null;
    }

}