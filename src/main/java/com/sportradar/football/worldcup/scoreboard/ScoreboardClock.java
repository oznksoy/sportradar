package com.sportradar.football.worldcup.scoreboard;

import java.time.Clock;
import java.time.LocalDateTime;

class ScoreboardClock {

    public LocalDateTime fetchTime() {
        return LocalDateTime.now(fetchClock());
    }

    public Clock fetchClock() {
        return Clock.systemDefaultZone();
    }

}
