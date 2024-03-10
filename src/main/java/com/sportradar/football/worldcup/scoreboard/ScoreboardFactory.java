package com.sportradar.football.worldcup.scoreboard;

public class ScoreboardFactory {

    private ScoreboardFactory() {
    }

    public static Scoreboard getScoreboard() {
        return new ScoreboardImp(ScoreboardCache.getInstance(), new ScoreboardClock(), new ScoreboardAudit());
    }

}
