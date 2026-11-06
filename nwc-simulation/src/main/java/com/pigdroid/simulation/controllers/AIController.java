package com.pigdroid.simulation.controllers;

import com.badlogic.ashley.core.Entity;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.PlayerComponent;
import com.pigdroid.simulation.components.PositionComponent;
import com.pigdroid.simulation.fsm.PlayerState;

public class AIController implements PlayerController {
    private float moveX;
    private float moveY;
    private boolean passPressed;
    private boolean shootPressed;
    private boolean tacklePressed;
    private boolean slidePressed;
    private boolean specialPressed;

    @Override
    public float getMoveX() { return moveX; }

    @Override
    public float getMoveY() { return moveY; }

    @Override
    public boolean isPassPressed() { return passPressed; }

    @Override
    public boolean isShootPressed() { return shootPressed; }

    @Override
    public boolean isTacklePressed() { return tacklePressed; }

    @Override
    public boolean isSlidePressed() { return slidePressed; }

    @Override
    public boolean isSpecialPressed() { return specialPressed; }

    @Override
    public void update(float deltaTime, Entity player, Entity ball) {
        resetInputs();

        if (player == null || ball == null) return;

        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        PositionComponent playerPos = player.getComponent(PositionComponent.class);

        BallComponent ballComponent = ball.getComponent(BallComponent.class);
        PositionComponent ballPos = ball.getComponent(PositionComponent.class);

        if (playerComponent == null || playerPos == null || ballComponent == null || ballPos == null) return;

        if (playerComponent.hp <= 0) {
            playerComponent.currentState = PlayerState.UNCONSCIOUS;
            return;
        }

        boolean hasBall = (ballComponent.owner == player);
        boolean myTeamHasBall = ballComponent.owner != null &&
            ballComponent.owner.getComponent(PlayerComponent.class) != null &&
            ballComponent.owner.getComponent(PlayerComponent.class).teamId == playerComponent.teamId;

        // Basic Nintendo World Cup style logic
        if (hasBall) {
            // Move towards opponent goal
            // In a real match we would check pitch direction, assuming team 0 moves right
            if (playerComponent.teamId == 0) {
                moveX = 1.0f; // Move right
                if (playerPos.x > 90) {
                    specialPressed = true;
                    shootPressed = false;
                } else if (playerPos.x > 80) {
                    shootPressed = true;
                }
            } else {
                moveX = -1.0f; // Move left
                if (playerPos.x < -90) {
                    specialPressed = true;
                    shootPressed = false;
                } else if (playerPos.x < -80) {
                    shootPressed = true;
                }
            }
            playerComponent.currentState = PlayerState.DRIBBLING;

        } else if (!myTeamHasBall) {
            // Chase ball
            float dx = ballPos.x - playerPos.x;
            float dy = ballPos.y - playerPos.y;

            float length = (float) Math.sqrt(dx * dx + dy * dy);

            if (ballComponent.owner == null && ballComponent.z > 2.0f && length < 3.0f) {
                specialPressed = true;
                tacklePressed = false;
                playerComponent.currentState = PlayerState.BACKFLIP_KICK;
            } else {
                if (length > 0) {
                    moveX = dx / length;
                    moveY = dy / length;
                }

                playerComponent.currentState = PlayerState.CHASING_BALL;

                // Tackle if close
                if (length < 2.0f && ballComponent.owner != null) {
                    tacklePressed = true;
                    playerComponent.currentState = PlayerState.TACKLING;
                } else if (length >= 2.0f && length < 5.0f && ballComponent.owner != null) {
                    slidePressed = true;
                    playerComponent.currentState = PlayerState.SLIDING;
                }
            }
        } else {
            // My team has ball but not me, position oneself or move forward
            playerComponent.currentState = PlayerState.IDLE;
            if (playerComponent.teamId == 0) moveX = 0.5f;
            else moveX = -0.5f;
        }
    }

    private void resetInputs() {
        moveX = 0;
        moveY = 0;
        passPressed = false;
        shootPressed = false;
        tacklePressed = false;
        slidePressed = false;
        specialPressed = false;
    }
}
