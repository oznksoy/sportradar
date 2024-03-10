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

    @Mock private ScoreboardClock clockMock;

    @BeforeEach
    public void init() {
        when(clockMock.fetchClock()).thenReturn(fetchTestClock("2024-03-01T21:35:30Z"));
        this.scoreboard = new ScoreboardImp(ScoreboardCache.getInstance(), clockMock, new ScoreboardAudit());
    }

    @Test
    void givenScoreboardIsEmpty_WhenMatchStarted_ThenMatchIsAddedToTheScoreboard() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",6, 6), actual);
    }

    @Test
    void givenScoreboardIsEmpty_WhenValidStartMatchInputIsReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",6, 6), actual);
    }

    @Test
    void givenEmptyWhenMatchStartedAndThenUpdated() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 1, 2);
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",6, 6), actual);
    }

    @Test
    void givenEmptyWhenMultipleMatchesStartedAndThenUpdated() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",6, 6),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z",0, 5)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenASetOfMatches_WhenUpdatedAndSummaryRequested() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z",0, 0),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z",0, 0),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z",0, 0),
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",0, 0),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z",0, 0)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenASetOfMatches_WhenUpdatedAndSummaryRequested_ThenReceiveAsSorted() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        List<Match> actual = scoreboard.summary();
          List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z",6, 6),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z",10, 2),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z",0, 5),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z",3, 1),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z",2, 2)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    private Clock fetchTestClock(String timeClause) {
        return Clock.fixed(
                Instant.parse(timeClause),
                ZoneOffset.UTC);
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