package com.sportradar.football.worldcup.scoreboard;

import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardConsistencyException;
import com.sportradar.football.worldcup.scoreboard.exception.ScoreboardInputException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class ScoreboardImp implements Scoreboard {

    //TODO: logger could be implemented
    //Logger logger = Logger.getLogger(this.getClass().getName());

    private final ScoreboardCache cache;
    private final ScoreboardClock clock;
    private final ScoreboardAudit audit;

    public ScoreboardImp(ScoreboardCache cache, ScoreboardClock clock, ScoreboardAudit audit) {
        this.cache = cache;
        this.clock = clock;
        this.audit = audit;
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        TeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustNotHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        cache.put(teamPair, initiateDetails());
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        TeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        Details details = cache.getDetails(teamPair);
        audit.checkScoreConsistency(
                homeTeamScore,
                awayTeamScore,
                details.getScore().getHomeScore(),
                details.getScore().getAwayScore()
        );
        cache.put(teamPair, fillDetails(homeTeamScore, awayTeamScore, details.getStartTime()));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) throws ScoreboardInputException, ScoreboardConsistencyException {
        audit.checkInputValidity(homeTeam, awayTeam);
        TeamPair teamPair = fillTeamPair(homeTeam, awayTeam);
        audit.checkIfMustHaveEntry(cache.hasEntry(teamPair), homeTeam, awayTeam);
        cache.remove(teamPair);
    }

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

    private TeamPair fillTeamPair(String homeTeam, String awayTeam) {
        TeamPair teamPair = new TeamPair();
        teamPair.setHomeTeam(homeTeam);
        teamPair.setAwayTeam(awayTeam);
        return teamPair;
    }

    private Details fillDetails(int homeTeamScore, int awayTeamScore, LocalDateTime matchTime) {
        Details details = new Details();
        details.setScore(fillScore(homeTeamScore, awayTeamScore));
        details.setStartTime(matchTime);
        return details;
    }

    private Details initiateDetails() {
        Details details = new Details();
        details.setScore(initiateScore());
        details.setStartTime(clock.fetchTime());
        return details;
    }

    private Score initiateScore() {
        Score score = new Score();
        score.setHomeScore(0);
        score.setAwayScore(0);
        return score;
    }

    private Score fillScore(int homeTeamScore, int awayTeamScore) {
        Score score = new Score();
        score.setHomeScore(homeTeamScore);
        score.setAwayScore(awayTeamScore);
        return score;
    }

    private Match decorateMatch(Map.Entry<TeamPair, Details> entry) {
        Match match = new Match();
        match.setHomeTeam(entry.getKey().getHomeTeam());
        match.setAwayTeam(entry.getKey().getAwayTeam());
        match.setMatchTime(entry.getValue().getStartTime());
        match.setHomeScore(entry.getValue().getScore().getHomeScore());
        match.setAwayScore(entry.getValue().getScore().getAwayScore());
        return match;
    }

}