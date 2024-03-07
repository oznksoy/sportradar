package com.sportradar.football.worldcup;

public enum MatchStatus {

    UPCOMING(0), ONGOING(1), FINISHED(2);

    private final int state;

    MatchStatus(int state){
        this.state = state;
    }

    public int getState() {
        return state;
    }

}
