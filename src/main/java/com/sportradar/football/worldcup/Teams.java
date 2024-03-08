package com.sportradar.football.worldcup;

import java.io.Serializable;

public class Teams implements Serializable {

    private String homeTeam;

    private String awayTeam;

    public Teams(String homeTeam, String awayTeam){
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
