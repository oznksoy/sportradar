package com.sportradar.football.worldcup.scoreboard;

/**
 * Accessor factory Entity for Scoreboard calls and injections.
 */
public class ScoreboardFactory {

    private ScoreboardFactory() {
    }

    /**
     * Calls on the Scoreboard and creates a new Scoreboard Instance.
     *
     * @return Scoreboard with a Shared Memory
     */
    public static Scoreboard getScoreboard() {
        return new ScoreboardImp(new ScoreboardCache(), new ScoreboardClock(), new ScoreboardAudit());
    }

}
