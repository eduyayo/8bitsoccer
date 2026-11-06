package com.nwc.simulation.config;

public class MatchConfig {
    public TeamConfig team1;
    public TeamConfig team2;

    public static MatchConfig createDefault() {
        MatchConfig config = new MatchConfig();
        config.team1 = TeamConfig.createDefault("Team 1", 6);
        config.team2 = TeamConfig.createDefault("Team 2", 6);
        return config;
    }
}
