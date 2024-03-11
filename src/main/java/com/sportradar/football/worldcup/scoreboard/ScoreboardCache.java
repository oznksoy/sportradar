package com.sportradar.football.worldcup.scoreboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-Safe, fast read, locked write data structure based internal scoreboard cache singleton. It stores the match in atomized data entities.
 */
class ScoreboardCache {

    private static ScoreboardCache instance;

    /**
     * Score board cache. In-built mechanisms of @link {@link ConcurrentHashMap} collection enables desired fast read and securely locked write operations by default.
     */
    private final Map<TeamPair, Details> scoreboard;

    private ScoreboardCache() {
        this.scoreboard = new ConcurrentHashMap<>();
    }

    /**
     * Call Scoreboard cache singular instance.
     *
     * @return Scoreboard cache singleton.
     */
    public static ScoreboardCache getInstance() {
        if (instance == null) {
            instance = new ScoreboardCache();

        }
        return instance;
    }

    public boolean hasEntry(TeamPair teamPair) {
        return scoreboard.containsKey(teamPair);
    }

    public Details getDetails(TeamPair teamPair) {
        return scoreboard.get(teamPair);
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

    void cleanCache() {
        this.scoreboard.clear();
    }

}
