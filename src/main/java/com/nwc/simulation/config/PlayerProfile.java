package com.nwc.simulation.config;

public class PlayerProfile {
    public String name;
    public int number;
    public String preferredPosition;

    public PhysicalAttributes physical = new PhysicalAttributes();
    public TechnicalAttributes technical = new TechnicalAttributes();
    public MentalAttributes mental = new MentalAttributes();
    public HiddenAttributes hidden = new HiddenAttributes();

    public static PlayerProfile createDefault(int number) {
        PlayerProfile profile = new PlayerProfile();
        profile.name = "Player " + number;
        profile.number = number;
        profile.preferredPosition = "MID";

        profile.physical.topSpeed = 5f;
        profile.physical.stamina = 100f;

        profile.technical.tackling = 5f;
        profile.technical.finishing = 5f;

        return profile;
    }
}
