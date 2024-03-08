package com.sportradar.football.worldcup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-built mechanisms of @link {@link ConcurrentHashMap} collection enables desired fast read and securely locked write operations by default
 */
class ScoreboardCache {

    /**
     * Score board cache
     */
    private Map<Match, Score> cache;

    private ScoreboardCache(){
        this.cache = new ConcurrentHashMap<>();
    }

    private ScoreboardCache instance;

    public ScoreboardCache getInstance() {
        if(instance == null) {
            this.instance = new ScoreboardCache();

        }
        return instance;
    }

}
