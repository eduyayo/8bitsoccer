package com.nwc.simulation.config;

import java.util.ArrayList;
import java.util.List;

public class TeamConfig {
    public String name;
    public List<PlayerProfile> players = new ArrayList<>();

    public static TeamConfig createDefault(String name, int playerCount) {
        TeamConfig team = new TeamConfig();
        team.name = name;
        for (int i = 0; i < playerCount; i++) {
            team.players.add(PlayerProfile.createDefault(i));
        }
        return team;
    }
}
