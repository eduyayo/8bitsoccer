package com.nwc.simulation.components;

import com.badlogic.ashley.core.Component;
import com.nwc.simulation.fsm.PlayerState;
import com.nwc.simulation.config.PlayerProfile;

public class PlayerComponent implements Component {
    public int teamId;
    public int playerNumber;
    public PlayerState currentState;

    public PlayerProfile profile;
    public float stamina;
    public float speed; // Keep for now as topSpeed/speed alias
    public float tacklePower;
    public float shootPower;
    public float hp = 100f;

    public PlayerComponent(int teamId, int playerNumber, PlayerProfile profile) {
        this.teamId = teamId;
        this.playerNumber = playerNumber;
        this.profile = profile;
        this.currentState = PlayerState.IDLE;

        if (profile != null) {
            this.stamina = profile.physical.stamina;
            this.speed = profile.physical.topSpeed;
            this.tacklePower = profile.technical.tackling;
            this.shootPower = profile.technical.finishing;
        } else {
            this.stamina = 100f;
            this.speed = 5f;
            this.tacklePower = 5f;
            this.shootPower = 5f;
        }
    }
}
