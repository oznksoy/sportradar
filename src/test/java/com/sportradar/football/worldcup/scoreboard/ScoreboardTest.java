package com.sportradar.football.worldcup.scoreboard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreboardTest {

    private Scoreboard scoreboard;

    @Mock
    private ScoreboardClock clockMock;

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardIsEmpty_WhenInvalidStartMatchInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) {
        initScoreboard(new ScoreboardClock());
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.startMatch(homeTeam, awayTeam));
        Assertions.assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardHasMatch_WhenInvalidUpdateScoreInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) throws ScoreboardInputException {
        initScoreboard(new ScoreboardClock());
        scoreboard.startMatch("Mexico", "Canada");
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.updateScore(homeTeam, awayTeam, 1, 1));
        Assertions.assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardHasMatch_WhenInvalidFinishMatchInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) throws ScoreboardInputException {
        initScoreboard(new ScoreboardClock());
        scoreboard.startMatch("Mexico", "Canada");
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.finishMatch(homeTeam, awayTeam));
        Assertions.assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    private static Stream<Arguments> invalidInputScenarios() {
        return Stream.of(
                Arguments.of(null, "Canada", "An input value is null: Home Team"),
                Arguments.of("   ", "Canada", "An input value is blank: Home Team"),
                Arguments.of("Mexico", null, "An input value is null: Away Team"),
                Arguments.of("Mexico", "   ", "An input value is blank: Away Team"),
                Arguments.of(null, null, "An input value is null: Home Team"),
                Arguments.of("   ", "   ", "An input value is blank: Home Team")
                );
    }

    @Test
    void givenScoreboardIsEmpty_WhenValidStartMatchInputIsReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        List<Match> actual = scoreboard.summary();
        assertEquals(1, actual.size());
        assertEquals(decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30", 0, 0), actual.getFirst());
    }

    @Test
    void givenScoreboardIsEmpty_WhenMultipleValidStartMatchInputsAreReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30", 0, 0),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30", 0, 0),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30", 0, 0),
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30", 0, 0),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30", 0, 0)
        ).toList();
        assertEquals(5, actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    void givenScoreboardIsLoaded_WhenMultipleValidUpdateScoreInputsAreReceived_ThenExistingMatchScoresAreUpdated() throws ScoreboardInputException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30", 6, 6),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30", 10, 2),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30", 0, 5),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30", 3, 1),
                decorateMatch("Germany", "France", "2024-03-01T21:35:30", 2, 2)
        ).toList();
        assertEquals(5, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void givenScoreboardIsLoaded_AndHasDifferingTimeStamps_WhenMultipleValidUpdateScoreInputsAreReceived_ThenExistingMatchScoresAreUpdated() throws ScoreboardInputException {
        initScoreboard(clockMock);
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        scoreboard.startMatch("Mexico", "Canada");
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:36:30"));
        scoreboard.startMatch("Spain", "Brazil");
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:37:30"));
        scoreboard.startMatch("Germany", "France");
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:38:30"));
        scoreboard.startMatch("Uruguay", "Italy");
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:39:30"));
        scoreboard.startMatch("Argentina", "Australia");

        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:38:30", 6, 6),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:36:30", 10, 2),
                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30", 0, 5),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:39:30", 3, 1),
                decorateMatch("Germany", "France", "2024-03-01T21:37:30", 2, 2)
        ).toList();
        assertEquals(5, actual.size());
        assertIterableEquals(expected, actual);
    }

//    @Test
//    void givenEmptyWhenMatchStartedAndThenUpdated() throws ScoreboardInputException {
//        scoreboard.startMatch("Mexico", "Canada");
//        scoreboard.updateScore("Mexico", "Canada", 1, 2);
//        Match actual = scoreboard.summary().getFirst();
//        assertEquals(decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6), actual);
//    }
//
//    @Test
//    void givenEmptyWhenMultipleMatchesStartedAndThenUpdated() throws ScoreboardInputException {
//        scoreboard.startMatch("Mexico", "Canada");
//        scoreboard.updateScore("Mexico", "Canada", 0, 5);
//        scoreboard.startMatch("Uruguay", "Italy");
//        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
//        List<Match> actual = scoreboard.summary();
//        List<Match> expected = Stream.of(
//                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6),
//                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 5)
//        ).toList();
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    void givenASetOfMatches_WhenUpdatedAndSummaryRequested() throws ScoreboardInputException {
//        scoreboard.startMatch("Mexico", "Canada");
//        scoreboard.startMatch("Spain", "Brazil");
//        scoreboard.startMatch("Germany", "France");
//        scoreboard.startMatch("Uruguay", "Italy");
//        scoreboard.startMatch("Argentina", "Australia");
//        List<Match> actual = scoreboard.summary();
//        List<Match> expected = Stream.of(
//                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 0),
//                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z", 0, 0),
//                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z", 0, 0),
//                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 0, 0),
//                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z", 0, 0)
//        ).toList();
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    void givenASetOfMatches_WhenUpdatedAndSummaryRequested_ThenReceiveAsSorted() throws ScoreboardInputException {
//        scoreboard.startMatch("Mexico", "Canada");
//        scoreboard.startMatch("Spain", "Brazil");
//        scoreboard.startMatch("Germany", "France");
//        scoreboard.startMatch("Uruguay", "Italy");
//        scoreboard.startMatch("Argentina", "Australia");
//        List<Match> actual = scoreboard.summary();
//        List<Match> expected = Stream.of(
//                decorateMatch("Uruguay", "Italy", "2024-03-01T21:35:30Z", 6, 6),
//                decorateMatch("Spain", "Brazil", "2024-03-01T21:35:30Z", 10, 2),
//                decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30Z", 0, 5),
//                decorateMatch("Argentina", "Australia", "2024-03-01T21:35:30Z", 3, 1),
//                decorateMatch("Germany", "France", "2024-03-01T21:35:30Z", 2, 2)
//        ).toList();
//        assertIterableEquals(expected, actual);
//    }

    // ************************
    // Supporting Test Methods:
    // ************************

    @AfterEach
    void afterEach(){
        this.scoreboard = null;
        ScoreboardCache.getInstance().cleanCache();
    }

    void initScoreboard(ScoreboardClock clock){
        this.scoreboard = new ScoreboardImp(ScoreboardCache.getInstance(), clock, new ScoreboardAudit());
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