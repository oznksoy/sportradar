package com.sportradar.football.worldcup.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreboardTest {

    private Scoreboard scoreboard;

    @Mock private ModuleClock moduleClock;

    private final String baseTimeClause = "2024-03-01T21:35:30Z";

    @BeforeEach
    public void init() {
        when(moduleClock.fetchClock()).thenReturn(fetchTestClock(baseTimeClause));
        this.scoreboard = new ScoreboardImp(ScoreboardCache.getInstance(), moduleClock);
    }

    private Clock fetchTestClock(String timeClause) {
        return Clock.fixed(
                Instant.parse(timeClause),
                ZoneOffset.UTC);
    }

    @Test
    void givenOneTeam_WhenMatchStarted() {
        scoreboard.startMatch("Mexico", "Canada");
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", baseTimeClause,6, 6), actual);
    }

    @Test
    void givenEmptyWhenMatchStartedAndThenUpdated(){
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 1, 2);
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", baseTimeClause,6, 6), actual);
    }

    @Test
    void givenEmptyWhenMultipleMatchesStartedAndThenUpdated(){
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", baseTimeClause,6, 6),
                decorateMatch("Mexico", "Canada", baseTimeClause,0, 5)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenASetOfMatches_WhenUpdatedAndSummaryRequested() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Mexico", "Canada", baseTimeClause,0, 0),
                decorateMatch("Spain", "Brazil", baseTimeClause,0, 0),
                decorateMatch("Germany", "France", baseTimeClause,0, 0),
                decorateMatch("Uruguay", "Italy", baseTimeClause,0, 0),
                decorateMatch("Argentina", "Australia", baseTimeClause,0, 0)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenASetOfMatches_WhenUpdatedAndSummaryRequested_ThenReceiveAsSorted() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        List<Match> actual = scoreboard.summary();
          List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", baseTimeClause,6, 6),
                decorateMatch("Spain", "Brazil", baseTimeClause,10, 2),
                decorateMatch("Mexico", "Canada", baseTimeClause,0, 5),
                decorateMatch("Argentina", "Australia", baseTimeClause,3, 1),
                decorateMatch("Germany", "France", baseTimeClause,2, 2)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    @Test
    void startMatch() {
    }

    @Test
    void updateScore() {
    }

    @Test
    void finishMatch() {
    }

    @Test
    void summarize() {
    }

    private Match decorateMatch(String home, String away, String time, int homeScore, int awayScore) {
        Match match = new Match();
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        match.setMatchTime(LocalDateTime.parse(time));
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        return match;
    }

}