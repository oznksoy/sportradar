package com.sportradar.football.worldcup.scoreboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-Safe, fast read, locked write data structure based internal scoreboard cache singleton. It stores the match in atomized data entities.
 *
 * @author Ozan Aksoy
 */
class ScoreboardCache {


    /**
     * Score board cache. In-built mechanisms of @link {@link ConcurrentHashMap} collection enables desired fast read and securely locked write operations by default.
     */
    private final Map<TeamPair, Details> scoreboard;

    public ScoreboardCache() {
        this.scoreboard = new ConcurrentHashMap<>();
    }

    /**
     * Returns whether if the team pair has a corresponding record in the cache.
     *
     * @param teamPair Team Pair as Key to check.
     * @return If key exists.
     */
    public boolean hasEntry(TeamPair teamPair) {
        return scoreboard.containsKey(teamPair);
    }

    /**
     * Returns corresponding match details.
     *
     * @param teamPair Team Pair as Key used in finding match details.
     * @return Match details.
     */
    public Details getDetails(TeamPair teamPair) {
        return scoreboard.get(teamPair);
    }

    /**
     * Store {@link TeamPair} - @{@link Details}  key - value pair in the scoreboard cache.
     *
     * @param teamPair Teams in the Match
     * @param details  Match Details
     */
    public void put(TeamPair teamPair, Details details) {
        scoreboard.put(teamPair, details);
    }

    /**
     * Removes the corresponding match from the pair.
     *
     * @param teamPair Corresponding key to the record.
     */
    public void remove(TeamPair teamPair) {
        this.scoreboard.remove(teamPair);
    }

    /**
     * Takes a snapshot of the current state of the scoreboard records and returns them as a map.
     *
     * @return A snapshot of scoreboard.
     */
    public Map<TeamPair, Details> snapshot() {
        return scoreboard.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> new TeamPair(entry.getKey()),
                        entry -> new Details(entry.getValue())
                )
        );
    }

}
