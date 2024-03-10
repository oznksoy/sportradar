package com.sportradar.football.worldcup.scoreboard;

import java.util.Objects;

class ScoreboardAudit {

    public static final String HOME_TEAM = "Home Team";
    public static final String AWAY_TEAM = "Away Team";

    public void checkInput(String homeTeam, String awayTeam) throws ScoreboardInputException {
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
