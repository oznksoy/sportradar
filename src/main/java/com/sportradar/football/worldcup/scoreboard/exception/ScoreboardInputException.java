package com.sportradar.football.worldcup.scoreboard.exception;

import java.io.IOException;

/**
 * Exception that signals that a scoreboard module input is not valid.
 *
 * @author Ozan Aksoy
 */
public final class ScoreboardInputException extends IOException {

    /**
     * Signals that a scoreboard input was not valid.
     *
     * @param message Explanation of the cause of exception
     */
    public ScoreboardInputException(String message) {
        super(message);
    }

}
