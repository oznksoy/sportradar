package com.sportradar.football.worldcup;

class MatchKeyGenerator {

    public MatchKey generateMatchEntry(String homeTeam, String awayTeam){
        return new MatchKey(homeTeam, awayTeam);
    }

}
