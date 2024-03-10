package com.sportradar.football.worldcup.scoreboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-built mechanisms of @link {@link ConcurrentHashMap} collection enables desired fast read and securely locked write operations by default
 */
class ScoreboardCache {

    private static ScoreboardCache instance;
    /**
     * Score board cache
     */
    private final Map<TeamPair, Details> scoreboard;

    private ScoreboardCache() {
        this.scoreboard = new ConcurrentHashMap<>();
    }

    public static ScoreboardCache getInstance() {
        if (instance == null) {
            instance = new ScoreboardCache();

        }
        return instance;
    }

    public void put(TeamPair teamPair, Details details) {
        scoreboard.put(teamPair, details);
    }

    public void remove(TeamPair teamPair) {
        this.scoreboard.remove(teamPair);
    }

    public Map<TeamPair, Details> snapshot() {
        return scoreboard.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> new TeamPair(entry.getKey()),
                        entry -> new Details(entry.getValue())
                )
        );
    }

    void cleanCache(){
        this.scoreboard.clear();
    }

}
