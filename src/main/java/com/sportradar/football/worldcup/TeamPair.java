package com.sportradar.football.worldcup;

import java.io.Serializable;

public class TeamPair implements Serializable {

    private String homeTeam;

    private String awayTeam;

    public TeamPair(String homeTeam, String awayTeam){
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

}
