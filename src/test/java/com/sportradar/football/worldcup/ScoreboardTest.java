package com.sportradar.football.worldcup;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private Scoreboard scoreboard;

    private MatchGenerator matchGenerator;

    private List<Match> generateTestMatches() {
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
        List<Match> matches = generateTestMatches();
        matches.forEach(match -> scoreboard.startMatch(match));
    }

    @Test
    void updateScore() {

    }

    @Test
    void finishMatch() {
    }

}