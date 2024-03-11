package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;

import java.util.List;

/**
 * <p>Scoreboard stored an incoming starting match data with team, start time and latest score details in a memory cache.
 * Data is not stored in a persistent data store. Scoreboard supports starting a match, updating score points,
 * finalizing a match, and generating a summary of the scoreboard.</p>
 * <br>
 * <p>Note that only running matches are stored, and finished matches are removed from the scoreboard and are not traced.</p>
 *
 * @author Ozan Aksoy
 */
public interface Scoreboard {

    /**
     * Initiates a match and registers it to the score board.
     *
     * @param homeTeam Home team
     * @param awayTeam Away team
     */
    void startMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException;

    /**
     * Receives a pair of absolute scores: home team score and away team score, then updates the scoreboard.
     *
     * @param homeTeamScore Home team score point
     * @param awayTeamScore Away team score point
     */
    void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) throws ScoreboardInputException, ScoreboardConsistencyException;

    /**
     * Removes a match from the scoreboard
     *
     * @param homeTeam Home team
     * @param awayTeam Away team
     */
    void finishMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException;

    /**
     * Fetches a summary of the Scoreboard.
     */
    List<Match> summary();

}
