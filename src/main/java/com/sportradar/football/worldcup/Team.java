package com.sportradar.football.worldcup;

import java.io.Serializable;

public class Team implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
