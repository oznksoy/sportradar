package com.sportradar.football.worldcup;

import java.util.logging.Logger;

public class Scoreboard {

    public void score(int value){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Value is :" + value);
    }

}