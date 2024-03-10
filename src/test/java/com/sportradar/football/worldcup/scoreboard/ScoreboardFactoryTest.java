package com.sportradar.football.worldcup.scoreboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardFactoryTest {

    @Test
    void whenScoreboardIsCalled_AnswersWithAnInstance() {
        assertNotNull(ScoreboardFactory.getScoreboard());
    }

}