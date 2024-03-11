package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;

import java.util.Objects;

/**
 * Package-private Validator mechanism for scoreboard workflow. Auditor is tasked with checker functions and acts as an internal audit for incoming data to Scoreboard.
 * It is designed to generate Scoreboard specific exceptions to terminate malformed states and requests.
 *
 * @author Ozan Aksoy
 */
class ScoreboardAudit {

    /**
     * Input home team field descriptor name
     */
    public static final String HOME_TEAM = "Home Team";

    /**
     * Input home team field descriptor name
     */
    public static final String HOME_TEAM_SCORE = "Home Team Score";

    /**
     * Input away team field descriptor name
     */
    public static final String AWAY_TEAM = "Away Team";

    /**
     * Input away team score field descriptor name
     */
    public static final String AWAY_TEAM_SCORE = "Away Team";

    /**
     * Checks if match already exists. If not so, then throws an exception to signal the caught match already started.
     *
     * @param hasEntry Flag to signal whether if the home-away team pair is already stored.
     * @param homeTeam Home team name
     * @param awayTeam Away team name
     * @throws ScoreboardConsistencyException Match already started exception.
     */
    public void checkIfMustNotHaveEntry(boolean hasEntry, String homeTeam, String awayTeam) throws ScoreboardConsistencyException {
        if (hasEntry) {
            throw new ScoreboardConsistencyException(String.format("This match(%s-%s) has already started.", homeTeam, awayTeam));
        }
    }

    /**
     * Checks if match does not exist, thus has not started yet. If not so, then throws an exception to signal the caught match has not started.
     *
     * @param hasEntry Flag to signal whether if the home-away team pair is already stored.
     * @param homeTeam Home team name
     * @param awayTeam Away team name
     * @throws ScoreboardConsistencyException Match that should have started, has not started exception.
     */
    public void checkIfMustHaveEntry(boolean hasEntry, String homeTeam, String awayTeam) throws ScoreboardConsistencyException {
        if (!hasEntry) {
            throw new ScoreboardConsistencyException(String.format("This match(%s-%s) has not been started.", homeTeam, awayTeam));
        }
    }

    /**
     * Checks if an incoming score value does not try to override a lower score to an existing match. If so, then throws an exception.
     *
     * @param homeScoreInput Home team score from incoming method input
     * @param awayScoreInput Away team score from incoming method input
     * @param homeScoreEntry Home team score from scoreboard
     * @param awayScoreEntry Away team score from scoreboard
     * @throws ScoreboardConsistencyException Input scores are trying to write lower scores from the existing match record.
     */
    public void checkScoreConsistency(int homeScoreInput, int awayScoreInput, int homeScoreEntry, int awayScoreEntry) throws ScoreboardConsistencyException {
        checkIfScoresAreExactSameWithEntry(homeScoreInput, awayScoreInput, homeScoreEntry, awayScoreEntry);
        checkIfScoresAreConsistent(HOME_TEAM_SCORE, homeScoreInput, homeScoreEntry);
        checkIfScoresAreConsistent(AWAY_TEAM_SCORE, awayScoreInput, awayScoreEntry);
    }

    /**
     * Checks if an incoming score request is an already existing state of the match scores. If so, then throws an exception.
     *
     * @param homeScoreInput Home team score from incoming method input
     * @param awayScoreInput Away team score from incoming method input
     * @param homeScoreEntry Home team score from scoreboard
     * @param awayScoreEntry Away team score from scoreboard
     * @throws ScoreboardConsistencyException Input scores will not be changing any existing record, thus it is a redundant request.
     */
    public void checkIfScoresAreExactSameWithEntry(int homeScoreInput, int awayScoreInput, int homeScoreEntry, int awayScoreEntry) throws ScoreboardConsistencyException {
        if (homeScoreInput == homeScoreEntry && awayScoreInput == awayScoreEntry) {
            throw new ScoreboardConsistencyException(
                    "The score input is already recorded.");
        }
    }

    /**
     * Checks if a given score field has received a lower score value then already existing score.
     *
     * @param inputName Input field descriptor name
     * @param inputScore incoming input score value
     * @param entryScore previously recorded input score value
     * @throws ScoreboardConsistencyException Input score is trying to write a lower score from the existing match record.
     */
    public void checkIfScoresAreConsistent(String inputName, int inputScore, int entryScore) throws ScoreboardConsistencyException {
        if (inputScore < entryScore) {
            throw new ScoreboardConsistencyException(
                    "An input value is inconsistent: " + inputName + ". " +
                            "The score can only be increased or unchanged.");
        }
    }

    /**
     * Checks if a given team name input value is blank or null. If so, then throws invalid input exception.
     *
     * @param homeTeam Home team input
     * @param awayTeam Away team input
     * @throws ScoreboardInputException Team input is invalid.
     */
    public void checkInputValidity(String homeTeam, String awayTeam) throws ScoreboardInputException {
        checkBlankInput(HOME_TEAM, homeTeam);
        checkBlankInput(AWAY_TEAM, awayTeam);
    }

    /**
     * Checks if a given team name input value is blank. If so, then throws invalid input exception.
     *
     * @param inputName Input field descriptor name
     * @param input actual input value
     * @throws ScoreboardInputException Team input is a blank value.
     */
    public void checkBlankInput(String inputName, String input) throws ScoreboardInputException {
        checkNullInput(inputName, input);
        if (input.isBlank()) {
            throw new ScoreboardInputException("An input value is blank: " + inputName);
        }
    }

    /**
     * Checks if a given team name input value is null. If so, then throws invalid input exception.
     *
     * @param inputName Input field descriptor name
     * @param input actual input value
     * @throws ScoreboardInputException Team input is null.
     */
    public void checkNullInput(String inputName, Object input) throws ScoreboardInputException {
        if (Objects.isNull(input)) {
            throw new ScoreboardInputException("An input value is null: " + inputName);
        }
    }

}
