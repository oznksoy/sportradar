package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Default base of the Scoreboard with Shared Memory.
 *
 * @author Ozan Aksoy
 */
class ScoreboardImp implements Scoreboard {

    /**
     * Scoreboard Cache.
     */
    private final ScoreboardCache cache;

    /**
     * Common Module Clock.
     */
    private final ScoreboardClock clock;

    /**
     * Common Scoreboard Audit.
     */
    private final ScoreboardAudit audit;

    /**
     * Default Scoreboard Implementation.
     *
     * @param cache Scoreboard Cache
     * @param clock Instance Clock
     * @param audit Instance Audit
     */
    public ScoreboardImp(ScoreboardCache cache, ScoreboardClock clock, ScoreboardAudit audit) {
        this.cache = cache;
        this.clock = clock;
        this.audit = audit;
    }

    /**
     * Initiates a match and registers it to the score board.
     *
     * @param homeTeam Home Team Name
     * @param awayTeam Away Team Name
     * @throws ScoreboardInputException       Signals invalid input such as blank or null values.
     * @throws ScoreboardConsistencyException Signals input that is inconsistent with the existing state of the Scoreboard.
     */
    @Override
    public void startMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        MatchTeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustNotHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        cache.put(teamPair, initiateDetails());
    }

    private MatchTeamPair fillTeamPair(String homeTeam, String awayTeam) {
        MatchTeamPair teamPair = new MatchTeamPair();
        teamPair.setHomeTeam(homeTeam);
        teamPair.setAwayTeam(awayTeam);
        return teamPair;
    }

    private MatchDetails initiateDetails() {
        MatchDetails details = new MatchDetails();
        details.setScore(initiateScore());
        details.setStartTime(clock.fetchTime());
        return details;
    }

    private MatchScore initiateScore() {
        MatchScore score = new MatchScore();
        score.setHomeScore(0);
        score.setAwayScore(0);
        return score;
    }

    /**
     * Receives a pair of absolute scores: home team score and away team score, then updates the scoreboard.
     *
     * @param homeTeamScore Home team score point
     * @param awayTeamScore Away team score point
     * @throws ScoreboardInputException       Signals invalid input such as blank or null values.
     * @throws ScoreboardConsistencyException Signals input that is inconsistent with the existing state of the Scoreboard.
     */
    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        MatchTeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        MatchDetails details = cache.getDetails(teamPair);
        audit.checkScoreConsistency(
                homeTeamScore,
                awayTeamScore,
                details.getScore().getHomeScore(),
                details.getScore().getAwayScore()
        );
        cache.put(teamPair, fillDetails(homeTeamScore, awayTeamScore, details.getStartTime()));
    }


    private MatchDetails fillDetails(int homeTeamScore, int awayTeamScore, LocalDateTime matchTime) {
        MatchDetails details = new MatchDetails();
        details.setScore(fillScore(homeTeamScore, awayTeamScore));
        details.setStartTime(matchTime);
        return details;
    }

    private MatchScore fillScore(int homeTeamScore, int awayTeamScore) {
        MatchScore score = new MatchScore();
        score.setHomeScore(homeTeamScore);
        score.setAwayScore(awayTeamScore);
        return score;
    }

    /**
     * Removes a match from the scoreboard
     *
     * @param homeTeam Home team
     * @param awayTeam Away team
     * @throws ScoreboardInputException       Signals invalid input such as blank or null values.
     * @throws ScoreboardConsistencyException Signals input that is inconsistent with the existing state of the Scoreboard.
     */
    @Override
    public void finishMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        MatchTeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        cache.remove(teamPair);
    }

    /**
     * Fetches a summary of the Scoreboard.
     */
    @Override
    public List<Match> summary() {
        return cache.snapshot().entrySet().stream()
                .map(this::decorateMatch)
                .sorted(getTotalScoreComparator().reversed().thenComparing(getMatchTimeComparator())).toList();
    }

    private Comparator<Match> getTotalScoreComparator() {
        return Comparator.comparingInt(match -> (match.getHomeScore() + match.getAwayScore()));
    }

    private Comparator<Match> getMatchTimeComparator() {
        return (match1, match2) -> match2.getMatchTime().compareTo(match1.getMatchTime());
    }

    private Match decorateMatch(Map.Entry<MatchTeamPair, MatchDetails> entry) {
        Match match = new Match();
        match.setHomeTeam(entry.getKey().getHomeTeam());
        match.setAwayTeam(entry.getKey().getAwayTeam());
        match.setMatchTime(entry.getValue().getStartTime());
        match.setHomeScore(entry.getValue().getScore().getHomeScore());
        match.setAwayScore(entry.getValue().getScore().getAwayScore());
        return match;
    }

}