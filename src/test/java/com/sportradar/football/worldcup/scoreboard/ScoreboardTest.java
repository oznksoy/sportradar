package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Scoreboard Test with standard/default scoreboard implementation class.
 * To understand the purpose and scenarios of the test cases, read the method names carefully.
 * The names are written in BDD key structure.
 */
@ExtendWith(MockitoExtension.class)
class ScoreboardTest {

    private Scoreboard scoreboard;

    @Mock
    private ScoreboardClock clockMock;

    //***********************************
    // Invalid Input Scenario Test Cases:
    //***********************************

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardIsEmpty_WhenInvalidStartMatchInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) {
        initScoreboard(new ScoreboardClock());
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.startMatch(homeTeam, awayTeam));
        Assertions.assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardHasMatch_WhenInvalidUpdateScoreInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) throws ScoreboardInputException, ScoreboardConsistencyException {
        initScoreboard(new ScoreboardClock());
        scoreboard.startMatch("Mexico", "Canada");
        ScoreboardInputException thrown = Assertions.assertThrows(ScoreboardInputException.class, () -> scoreboard.updateScore(homeTeam, awayTeam, 1, 1));
        Assertions.assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidInputScenarios")
    void givenScoreboardHasMatch_WhenInvalidFinishMatchInputIsReceived_ThenMatchIsNotAdded_AndExceptionIsThrown(String homeTeam, String awayTeam, String expectedExceptionMessage) throws ScoreboardInputException, ScoreboardConsistencyException {
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

    //****************************************
    // Inconsistent State Scenario Test Cases:
    //***************************************

    @Test
    void givenScoreboardIsEmpty_WhenValidStartMatchInputIsReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        List<Match> actual = scoreboard.summary();
        assertEquals(1, actual.size());
        assertEquals(decorateMatch("Mexico", "Canada", "2024-03-01T21:35:30", 0, 0), actual.getFirst());
    }

    @Test
    void givenScoreboardIsEmpty_WhenMultipleValidStartMatchInputsAreReceived_ThenANewMatchIsAddedToTheScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        initScoreboard(clockMock);
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
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
    void givenScoreboardIsLoaded_WhenExistingMatchIsAskedToBeStarted_ThenThrowAWarningMessage_AndDoNotAlterTheScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        ScoreboardConsistencyException thrown = Assertions.assertThrows(ScoreboardConsistencyException.class, () -> scoreboard.startMatch("Mexico", "Canada"));
        Assertions.assertEquals("This match(Mexico-Canada) has already started.", thrown.getMessage());
    }

    @Test
    void givenScoreboardIsLoaded_WhenUpdateScoreHasNotStarted_ThenThrowAWarningMessage_AndDoNotAlterTheScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 1, 2);
        ScoreboardConsistencyException thrown = Assertions.assertThrows(ScoreboardConsistencyException.class,
                () -> scoreboard.updateScore("Croatia", "Sweden", 2, 3));
        Assertions.assertEquals("This match(Croatia-Sweden) has not been started.", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("inconsistentUpdateScores")
    void givenScoreboardIsLoaded_WhenUpdateScoreIsInconsistent_ThenThrowAWarningMessage_AndDoNotAlterTheScoreboard(int homeScore, int awayScore, int homeScoreUpdate, int awayScoreUpdate, String msg) throws ScoreboardInputException, ScoreboardConsistencyException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", homeScore, awayScore);
        ScoreboardConsistencyException thrown = Assertions.assertThrows(ScoreboardConsistencyException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", homeScoreUpdate, awayScoreUpdate));
        Assertions.assertEquals(msg, thrown.getMessage());
    }

    private static Stream<Arguments> inconsistentUpdateScores() {
        return Stream.of(
                Arguments.of(3, 5, 0, 5, "An input value is inconsistent: Home Team Score. The score can only be increased or unchanged."),
                Arguments.of(2, 2, 5, 0, "An input value is inconsistent: Away Team. The score can only be increased or unchanged."),
                Arguments.of(3, 5, 0, 0, "An input value is inconsistent: Home Team Score. The score can only be increased or unchanged."),
                Arguments.of(3, 3, 3, 3, "The score input is already recorded.")
        );
    }

    @Test
    void givenScoreboardIsLoaded_WhenNonExistingMatchAskedToBeFinished_ThenThrowAWarningMessage_AndDoNotAlterTheScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
        initScoreboard(clockMock);
        scoreboard.startMatch("Mexico", "Canada");
        ScoreboardConsistencyException thrown = Assertions.assertThrows(ScoreboardConsistencyException.class, () -> scoreboard.finishMatch("Croatia", "Sweden"));
        Assertions.assertEquals("This match(Croatia-Sweden) has not been started.", thrown.getMessage());
    }

    //************************************
    // Expected Valid Scenario Test Cases:
    //************************************

    @Test
    void givenScoreboardIsLoaded_WhenMultipleValidUpdateScoreInputsAreReceived_ThenExistingMatchScoresAreUpdated() throws ScoreboardInputException, ScoreboardConsistencyException {
        initScoreboard(clockMock);
        when(clockMock.fetchTime()).thenReturn(LocalDateTime.parse("2024-03-01T21:35:30"));
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
    void givenScoreboardIsLoaded_AndHasDifferingTimeStamps_WhenMultipleValidUpdateScoreInputsAreReceived_ThenExistingMatchScoresAreUpdated() throws ScoreboardInputException, ScoreboardConsistencyException {
        injectUpdated5MatchLoadedScoreboardScenario();
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

    @Test
    void givenScoreboardIsLoaded_WhenAnExistingMatchIsFinished_ThenFinishedMatchIsRemovedFromScoreboard() throws ScoreboardInputException, ScoreboardConsistencyException {
        injectUpdated5MatchLoadedScoreboardScenario();
        scoreboard.finishMatch("Mexico", "Canada");
        List<Match> actual = scoreboard.summary();
        List<Match> expected = Stream.of(
                decorateMatch("Uruguay", "Italy", "2024-03-01T21:38:30", 6, 6),
                decorateMatch("Spain", "Brazil", "2024-03-01T21:36:30", 10, 2),
                decorateMatch("Argentina", "Australia", "2024-03-01T21:39:30", 3, 1),
                decorateMatch("Germany", "France", "2024-03-01T21:37:30", 2, 2)
        ).toList();
        assertEquals(4, actual.size());
        assertIterableEquals(expected, actual);
    }

    // *****************
    // Common Scenarios:
    // *****************

    private void injectUpdated5MatchLoadedScoreboardScenario() throws ScoreboardInputException, ScoreboardConsistencyException {
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
    }

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