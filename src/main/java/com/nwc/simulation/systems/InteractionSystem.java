package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nwc.simulation.components.BallComponent;
import com.nwc.simulation.components.PlayerComponent;
import com.nwc.simulation.components.PositionComponent;
import com.nwc.simulation.fsm.PlayerState;

public class InteractionSystem extends IteratingSystem {
    private Entity ball;

    public InteractionSystem() {
        super(Family.all(PlayerComponent.class, PositionComponent.class).get());
    }

    public void setBall(Entity ball) {
        this.ball = ball;
    }

    @Override
    protected void processEntity(Entity player, float deltaTime) {
        if (ball == null) return;

        BallComponent ballComponent = ball.getComponent(BallComponent.class);
        PositionComponent ballPos = ball.getComponent(PositionComponent.class);
        PositionComponent playerPos = player.getComponent(PositionComponent.class);

        if (ballComponent == null || ballPos == null || playerPos == null) return;

        // Simple distance check to assign ball possession if no one has it
        if (ballComponent.owner == null) {
            float dx = ballPos.x - playerPos.x;
            float dy = ballPos.y - playerPos.y;
            float distanceSq = dx*dx + dy*dy;

            // If close enough and ball is on the ground (or low enough)
            if (distanceSq < 4.0f && ballComponent.z < 2.0f) {
                ballComponent.owner = player;
                player.getComponent(PlayerComponent.class).currentState = PlayerState.DRIBBLING;
            }
        }

        // If this player has the ball, make the ball follow the player
        if (ballComponent.owner == player) {
            ballPos.x = playerPos.x;
            ballPos.y = playerPos.y;
        }
    }
}
