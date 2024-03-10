package com.sportradar.football.worldcup.scoreboard;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

class ScoreboardImp implements Scoreboard {

    private final ScoreboardCache cache;
    private final ModuleClock clock;

    public ScoreboardImp(ScoreboardCache cache, ModuleClock clock) {
        this.cache = cache;
        this.clock = clock;
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        //TODO: Add checks and data audit
        cache.put(fillTeamPair(homeTeam, awayTeam), initiateDetails());
    }

    @Override
    public void updateScore(String homeTeam , String awayTeam, int homeTeamScore, int awayTeamScore) {
        //TODO: Add checks and data audit
        cache.put(fillTeamPair(homeTeam, awayTeam), fillDetails(homeTeamScore, awayTeamScore));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        //TODO: Add checks and data audit
        cache.remove(fillTeamPair(homeTeam, awayTeam));
    }

    @Override
    public List<Match> summary() {
        return cache.snapshot().entrySet().stream()
                .map(this::decorateMatch)
                .sorted(getComparator()).toList();
    }

    private TeamPair fillTeamPair(String homeTeam, String awayTeam){
        TeamPair teamPair = new TeamPair();
        teamPair.setHomeTeam(homeTeam);
        teamPair.setAwayTeam(awayTeam);
        return teamPair;
    }

    private Details fillDetails(int homeTeamScore, int awayTeamScore){
        Details details = new Details();
        details.setScore(fillScore(homeTeamScore, awayTeamScore));
        details.setMatchTime(clock.fetchTime());
        return details;
    }

    private Details initiateDetails(){
        Details details = new Details();
        details.setScore(initiateScore());
        details.setMatchTime(clock.fetchTime());
        return details;
    }

    private Score initiateScore(){
        Score score = new Score();
        score.setHomeScore(0);
        score.setAwayScore(0);
        return score;
    }

    private Score fillScore(int homeTeamScore, int awayTeamScore){
        Score score = new Score();
        score.setHomeScore(homeTeamScore);
        score.setAwayScore(awayTeamScore);
        return score;
    }

    private Match decorateMatch(Map.Entry<TeamPair, Details> entry){
        Match match = new Match();
        match.setHomeTeam(entry.getKey().getHomeTeam());
        match.setAwayTeam(entry.getKey().getAwayTeam());
        match.setMatchTime(entry.getValue().getMatchTime());
        match.setHomeScore(entry.getValue().getScore().getHomeScore());
        match.setAwayScore(entry.getValue().getScore().getAwayScore());
        return match;
    }

    private Comparator<Match> getComparator() {
        //TODO: consider comparator chain with functional interfaces
        return (match1, match2) -> {
            int totalScore1 = match1.getHomeScore() + match1.getAwayScore();
            int totalScore2 = match2.getHomeScore() + match2.getAwayScore();
            int result = totalScore2 - totalScore1;
            if (result == 0) {
                return match1.getMatchTime().compareTo(match2.getMatchTime());
            }
            return result;
        };
    }

}