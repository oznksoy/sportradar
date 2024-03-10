package com.sportradar.football.worldcup.scoreboard;

import java.util.List;

public interface Scoreboard {

    /**
     * Initiates a match and registers it to the score board.
     *
     * @param homeTeam Home team
     * @param awayTeam Away team
     */
    public void startMatch(String homeTeam, String awayTeam);

    /**
     * Receives a pair of absolute scores: home team score and away team score, then updates the scoreboard.
     *
     * @param homeTeamScore Home team score point
     * @param awayTeamScore Away team score point
     */
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore);

    /**
     * Removes a match from the scoreboard
     *
     * @param homeTeam Home team
     * @param awayTeam Away team
     */
    public void finishMatch(String homeTeam, String awayTeam);

    /**
     * Fetches a summary of the Scoreboard.
     */
    List<Match> summary();

}
