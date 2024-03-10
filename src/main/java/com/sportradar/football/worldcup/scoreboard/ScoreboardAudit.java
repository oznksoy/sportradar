package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;

import java.util.Objects;

class ScoreboardAudit {

    public static final String HOME_TEAM = "Home Team";
    public static final String HOME_TEAM_SCORE = "Home Team Score";
    public static final String AWAY_TEAM = "Away Team";
    public static final String AWAY_TEAM_SCORE = "Away Team";

    public void checkIfMustNotHaveEntry(boolean hasEntry, String homeTeam, String awayTeam) throws ScoreboardConsistencyException {
        if (hasEntry) {
            throw new ScoreboardConsistencyException(String.format("This match(%s-%s) has already started.", homeTeam, awayTeam));
        }
    }

    public void checkIfMustHaveEntry(boolean hasEntry, String homeTeam, String awayTeam) throws ScoreboardConsistencyException {
        if (!hasEntry) {
            throw new ScoreboardConsistencyException(String.format("This match(%s-%s) has not been started.", homeTeam, awayTeam));
        }
    }

    public void checkScoreConsistency(int homeScoreInput, int awayScoreInput, int homeScoreEntry, int awayScoreEntry) throws ScoreboardConsistencyException {
        checkIfScoresAreExactSameWithEntry(homeScoreInput, awayScoreInput, homeScoreEntry, awayScoreEntry);
        checkIfScoresAreConsistent(HOME_TEAM_SCORE, homeScoreInput, homeScoreEntry);
        checkIfScoresAreConsistent(AWAY_TEAM_SCORE, awayScoreInput, awayScoreEntry);
    }

    public void checkIfScoresAreExactSameWithEntry(int homeScoreInput, int awayScoreInput, int homeScoreEntry, int awayScoreEntry) throws ScoreboardConsistencyException {
        if (homeScoreInput == homeScoreEntry && awayScoreInput == awayScoreEntry) {
            throw new ScoreboardConsistencyException(
                    "The score input is already recorded.");
        }
    }

    public void checkIfScoresAreConsistent(String inputName, int inputScore, int entryScore) throws ScoreboardConsistencyException {
        if (inputScore < entryScore) {
            throw new ScoreboardConsistencyException(
                    "An input value is inconsistent: " + inputName + ". " +
                            "The score can only be increased or unchanged.");
        }
    }

    public void checkInputValidity(String homeTeam, String awayTeam) throws ScoreboardInputException {
        checkBlankInput(HOME_TEAM, homeTeam);
        checkBlankInput(AWAY_TEAM, awayTeam);
    }

    public void checkBlankInput(String inputName, String input) throws ScoreboardInputException {
        checkNullInput(inputName, input);
        if (input.isBlank()) {
            throw new ScoreboardInputException("An input value is blank: " + inputName);
        }
    }

    public void checkNullInput(String inputName, Object input) throws ScoreboardInputException {
        if (Objects.isNull(input)) {
            throw new ScoreboardInputException("An input value is null: " + inputName);
        }
    }

}
