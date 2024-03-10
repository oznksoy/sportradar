package com.sportradar.football.worldcup.scoreboard;

import java.util.Objects;

class ScoreboardAudit {

    public static final String HOME_TEAM = "Home Team";
    public static final String HOME_TEAM_SCORE = "Home Team Score";
    public static final String AWAY_TEAM = "Away Team";
    public static final String AWAY_TEAM_SCORE = "Away Team";

    public void checkIfMustNotHaveEntry(boolean hasEntry) throws ScoreboardInputException {
        if (hasEntry) {
            throw new ScoreboardInputException("This match has already started.");
        }
    }

    public void checkIfMustHaveEntry(boolean hasEntry) throws ScoreboardInputException {
        if (!hasEntry) {
            throw new ScoreboardInputException("This match has not been started.");
        }
    }

    public void checkScoreConsistency(int homeScoreInput, int awayScoreInput, int homeScoreEntry, int awayScoreEntry) throws ScoreboardInputException {
        checkIfScoresAreConsistent(HOME_TEAM_SCORE, homeScoreInput, homeScoreEntry);
        checkIfScoresAreConsistent(AWAY_TEAM_SCORE, awayScoreInput, awayScoreEntry);
    }

    public void checkIfScoresAreConsistent(String inputName, int inputScore, int entryScore) throws ScoreboardInputException {
        if (inputScore < entryScore) {
            throw new ScoreboardInputException(
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
