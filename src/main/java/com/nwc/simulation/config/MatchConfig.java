package com.nwc.simulation.config;

public class MatchConfig {
    public long seed;
    public TeamConfig team1;
    public TeamConfig team2;
    public FieldType fieldType = FieldType.GRASS;
    public WeatherCondition weatherCondition = WeatherCondition.CLEAR;

    public static MatchConfig createDefault() {
        MatchConfig config = new MatchConfig();
        config.seed = 0L;
        config.team1 = TeamConfig.createDefault("Team 1", 6);
        config.team2 = TeamConfig.createDefault("Team 2", 6);
        return config;
    }
}
