package com.sportradar.football.worldcup;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class ScoreboardTest {

    private Scoreboard scoreboard;

    private List<TeamPair> generateTestPairs() {
        return Stream.of(
                new TeamPair("Mexico", "Canada"),
                new TeamPair("Spain", "Brazil"),
                new TeamPair("Germany", "France"),
                new TeamPair("Uruguay", "Italy"),
                new TeamPair("Argentina", "Australia")
        ).toList();
    }

    @Test
    void startMatch() {
        List<TeamPair> list = generateTestPairs();
        list.forEach(teams -> scoreboard.startMatch(teams));
        List<Match> actual = scoreboard.summarize();
    }

    @Test
    void updateScore() {

    }

    @Test
    void finishMatch() {
    }

}