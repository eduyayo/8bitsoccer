package com.nwc.simulation.fsm;

import com.badlogic.ashley.core.Entity;
import com.nwc.simulation.components.PlayerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(PlayerStateMachine.class);

    public void update(Entity playerEntity, float deltaTime) {
        PlayerComponent playerComponent = playerEntity.getComponent(PlayerComponent.class);
        if (playerComponent == null) return;

        PlayerState previousState = playerComponent.currentState;

        // Basic transition logic based on current state
        switch (playerComponent.currentState) {
            case IDLE:
                // Logic to transition out of idle
                break;
            case CHASING_BALL:
                break;
            case DRIBBLING:
                break;
            case TACKLING:
                // E.g., return to IDLE after tackle animation/time finishes
                break;
            case PASSING:
            case SHOOTING:
                // Return to IDLE after action completes
                playerComponent.currentState = PlayerState.IDLE;
                break;
            case DOWNED:
                // Recovery logic
                break;
        }

        if (previousState != playerComponent.currentState) {
            logger.debug("Player {}_{} transitioned from {} to {}",
                playerComponent.teamId, playerComponent.playerNumber, previousState, playerComponent.currentState);
        }
    }
}
