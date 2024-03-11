package com.sportradar.football.worldcup.scoreboard.exception;

import java.io.IOException;

/**
 * Exception that signals that a data inconsistency with the incoming input request was detected.
 *
 * @author Ozan Aksoy
 */
public class ScoreboardConsistencyException extends IOException {

    /**
     * Signals that a data inconsistency with the incoming input request was detected.
     *
     * @param message Explanation of the cause of exception
     */
    public ScoreboardConsistencyException(String message) {
        super(message);
    }

}
