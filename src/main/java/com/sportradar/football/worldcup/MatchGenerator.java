package com.sportradar.football.worldcup;

class MatchGenerator {

    public Teams generateMatch(String homeTeam, String awayTeam){
        return new Teams(homeTeam, awayTeam);
    }

}
