package com.sportradar.football.worldcup;

import java.io.Serializable;
import java.util.SortedMap;

public class Match implements Serializable {

    private TeamPair pair;

    private Details details;

    public Match(TeamPair pair, Details details) {
        this.pair = pair;
        this.details = details;
    }

    public TeamPair getPair() {
        return pair;
    }

    public void setPair(TeamPair pair) {
        this.pair = pair;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

}
