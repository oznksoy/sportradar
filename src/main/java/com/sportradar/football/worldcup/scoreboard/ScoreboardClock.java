package com.sportradar.football.worldcup.scoreboard;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Common module clock implementation for Scoreboard module. This internal clock layer enables manipulation and encapsulation of the time based operations within the module when necessary.
 *
 * @author Ozan Aksoy
 */
class ScoreboardClock {

    /**
     * Return module set time from the module clock.
     *
     * @return Configured module time.
     */
    public LocalDateTime fetchTime() {
        return LocalDateTime.now(fetchClock());
    }

    /**
     * Returns configured module clock.
     *
     * @return Module Clock.
     */
    public Clock fetchClock() {
        return Clock.systemDefaultZone();
    }

}
