package com.sportradar.football.worldcup;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class ScoreboardTest {

    private Scoreboard scoreboard;

    private TeamPairGenerator matchGenerator;

    private List<TeamPair> generateTestMatches() {
        return Stream.of(
                matchGenerator.generateMatch("Mexico", "Canada"),
                matchGenerator.generateMatch("Spain", "Brazil"),
                matchGenerator.generateMatch("Germany", "France"),
                matchGenerator.generateMatch("Uruguay", "Italy"),
                matchGenerator.generateMatch("Argentina", "Australia")
        ).toList();
    }

    @Test
    void startMatch() {
        List<TeamPair> list = generateTestMatches();
        list.forEach(teams -> scoreboard.startMatch(teams));
    }

    @Test
    void updateScore() {

    }

    @Test
    void finishMatch() {
    }

}