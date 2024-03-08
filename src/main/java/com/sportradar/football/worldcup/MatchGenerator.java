package com.sportradar.football.worldcup;

class MatchGenerator {

    public Match generateMatch(String homeTeam, String awayTeam){
        return new Match(homeTeam, awayTeam);
    }

}
