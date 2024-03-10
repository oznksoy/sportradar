package com.sportradar.football.worldcup.scoreboard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    @Mock
    private ScoreboardClock clockMock;

    @Test
    void givenScoreboardIsEmpty_WhenMatchStarted_AndHomeTeamIsNull_ThenMatchIsNotAdded_AndExceptionIsThrown() {
        initScoreboard(new ScoreboardClock());
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.startMatch(null, "Canada"));
        Assertions.assertEquals("An input value is null: Home Team", thrown.getMessage());
    }

    @Test
    void givenScoreboardIsEmpty_WhenMatchStarted_AndAwayTeamIsNull_ThenMatchIsNotAdded_AndExceptionIsThrown() {
        initScoreboard(new ScoreboardClock());
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.startMatch("Canada", null));
        Assertions.assertEquals("An input value is null: Away Team", thrown.getMessage());
    }

    @Test
    void givenScoreboardIsEmpty_WhenMatchStarted_AndInputValuesAreNull_ThenMatchIsNotAdded_AndExceptionIsThrown() {
        initScoreboard(new ScoreboardClock());
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.startMatch(null, null));
        Assertions.assertEquals("An input value is null: Home Team", thrown.getMessage());
    }

    @Test
    void givenScoreboardIsEmpty_WhenValidStartMatchInputIsReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 0, 0), actual);
    }

    @Test
    void givenEmptyWhenMatchStartedAndThenUpdated() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 1, 2);
        Match actual = scoreboard.summary().getFirst();
        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6), actual);
    }

    @Test
    void givenEmptyWhenMultipleMatchesStartedAndThenUpdated() throws ScoreboardInputException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 5)
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
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 0),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z", 0, 0),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z", 0, 0),
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 0, 0),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z", 0, 0)
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
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z", 10, 2),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 5),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z", 3, 1),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z", 2, 2)
        ).toList();
        assertIterableEquals(expected, actual);
    }

    // ************************
    // Supporting Test Methods:
    // ************************

    void initScoreboard(ScoreboardClock clock){
        this.scoreboard = new ScoreboardImp(ScoreboardCache.getInstance(), clock, new ScoreboardAudit());
    }

    @AfterEach
     void afterEach(){
        this.scoreboard = null;
        ScoreboardCache.getInstance().cleanCache();
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