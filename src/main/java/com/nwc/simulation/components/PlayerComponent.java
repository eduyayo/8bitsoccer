package com.nwc.simulation.components;

import com.badlogic.ashley.core.Component;
import com.nwc.simulation.fsm.PlayerState;

public class PlayerComponent implements Component {
    public int teamId;
    public int playerNumber;
    public PlayerState currentState;
    public float stamina;
    public float speed;
    public float tacklePower;
    public float shootPower;

    public PlayerComponent(int teamId, int playerNumber) {
        this.teamId = teamId;
        this.playerNumber = playerNumber;
        this.currentState = PlayerState.IDLE;
        this.stamina = 100f;
        this.speed = 5f;
        this.tacklePower = 5f;
        this.shootPower = 5f;
    }
}
