package com.sportradar.football.worldcup.scoreboard;

/**
 * Accessor factory Entity for Scoreboard calls and injections.
 */
public class ScoreboardFactory {

    private ScoreboardFactory() {
    }

    /**
     * Calls on the Scoreboard and creates a new Scoreboard Instance.
     * Currently, all Scoreboards uses the same cache instance.
     * Thus,1 the memory is shared between Scoreboard instances.
     *
     * @return Scoreboard with a Shared Memory
     */
    public static Scoreboard getScoreboard() {
        return new ScoreboardImp(ScoreboardCache.getInstance(), new ScoreboardClock(), new ScoreboardAudit());
    }

}
