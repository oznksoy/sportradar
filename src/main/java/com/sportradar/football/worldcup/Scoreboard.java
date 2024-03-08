package com.sportradar.football.worldcup;

public class Scoreboard {

    private MatchGenerator matchGenerator;

    /**
     * Initiates a match and registers it to the score board.
     */
    public void startMatch(String homeTeam, String awayTeam) {
        startMatch(matchGenerator.generateMatch(homeTeam, awayTeam));
    }

    public void startMatch(Teams teams) {

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

}