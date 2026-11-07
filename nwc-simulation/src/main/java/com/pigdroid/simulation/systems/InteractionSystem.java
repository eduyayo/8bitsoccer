package com.pigdroid.simulation.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.PlayerComponent;
import com.pigdroid.simulation.components.PositionComponent;
import com.pigdroid.simulation.components.ControllerComponent;
import com.pigdroid.simulation.components.VelocityComponent;
import com.pigdroid.simulation.controllers.PlayerController;
import com.pigdroid.simulation.fsm.PlayerState;

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
                ballComponent.lastTouchedBy = player;
                player.getComponent(PlayerComponent.class).currentState = PlayerState.DRIBBLING;
            }
        }

        ControllerComponent ctrl = player.getComponent(ControllerComponent.class);
        PlayerController controller = ctrl != null ? ctrl.controller : null;

        if (controller != null && controller.isSpecialPressed() && ballComponent.owner == null && ballComponent.z > 2.0f) {
            float dx = ballPos.x - playerPos.x;
            float dy = ballPos.y - playerPos.y;
            if (dx * dx + dy * dy < 9.0f) {
                player.getComponent(PlayerComponent.class).currentState = PlayerState.BACKFLIP_KICK;
                VelocityComponent ballVelocityComponent = ball.getComponent(VelocityComponent.class);
                if (ballVelocityComponent != null) {
                    ballComponent.lastTouchedBy = player;
                    float mx = controller.getMoveX();
                    float my = controller.getMoveY();
                    float len = (float) Math.sqrt(mx * mx + my * my);
                    if (len > 0) {
                        ballVelocityComponent.x = (mx / len) * 30f;
                        ballVelocityComponent.y = (my / len) * 30f;
                    } else {
                        ballVelocityComponent.x = (player.getComponent(PlayerComponent.class).teamId == 0 ? 30f : -30f);
                        ballVelocityComponent.y = 0;
                    }
                    ballComponent.zVelocity = 15f;
                }
            }
        }

        if (controller != null && ballComponent.owner == player) {
            VelocityComponent ballVelocityComponent = ball.getComponent(VelocityComponent.class);
            if (ballVelocityComponent != null) {
                if (controller.isPassPressed()) {
                    ballComponent.owner = null;
                    ballComponent.lastTouchedBy = player;
                    float minDistanceSq = Float.MAX_VALUE;
                    Entity nearestTeammate = null;
                    PositionComponent nearestPos = null;

                    for (Entity other : getEngine().getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class).get())) {
                        if (other == player) continue;
                        PlayerComponent otherPlayerComp = other.getComponent(PlayerComponent.class);
                        if (otherPlayerComp.teamId == player.getComponent(PlayerComponent.class).teamId && otherPlayerComp.playerNumber != player.getComponent(PlayerComponent.class).playerNumber) {
                            PositionComponent otherPos = other.getComponent(PositionComponent.class);
                            float dx = otherPos.x - playerPos.x;
                            float dy = otherPos.y - playerPos.y;
                            float distSq = dx * dx + dy * dy;
                            if (distSq < minDistanceSq) {
                                minDistanceSq = distSq;
                                nearestTeammate = other;
                                nearestPos = otherPos;
                            }
                        }
                    }

                    if (nearestTeammate != null) {
                        float dx = nearestPos.x - playerPos.x;
                        float dy = nearestPos.y - playerPos.y;
                        float len = (float) Math.sqrt(dx * dx + dy * dy);
                        if (len > 0) {
                            ballVelocityComponent.x = (dx / len) * 15f;
                            ballVelocityComponent.y = (dy / len) * 15f;
                        }
                    } else {
                        ballVelocityComponent.x = (player.getComponent(PlayerComponent.class).teamId == 0 ? 15f : -15f);
                        ballVelocityComponent.y = 0;
                    }
                } else if (controller.isShootPressed()) {
                    ballComponent.owner = null;
                    ballComponent.lastTouchedBy = player;
                    float mx = controller.getMoveX();
                    float my = controller.getMoveY();
                    float len = (float) Math.sqrt(mx * mx + my * my);
                    if (len > 0) {
                        ballVelocityComponent.x = (mx / len) * 20f;
                        ballVelocityComponent.y = (my / len) * 20f;
                    } else {
                        ballVelocityComponent.x = (player.getComponent(PlayerComponent.class).teamId == 0 ? 20f : -20f);
                        ballVelocityComponent.y = 0;
                    }
                } else if (controller.isSpecialPressed()) {
                    ballComponent.owner = null;
                    ballComponent.lastTouchedBy = player;
                    float mx = controller.getMoveX();
                    float my = controller.getMoveY();
                    float len = (float) Math.sqrt(mx * mx + my * my);
                    if (len > 0) {
                        ballVelocityComponent.x = (mx / len) * 30f;
                        ballVelocityComponent.y = (my / len) * 30f;
                    } else {
                        ballVelocityComponent.x = (player.getComponent(PlayerComponent.class).teamId == 0 ? 30f : -30f);
                        ballVelocityComponent.y = 0;
                    }
                    ballComponent.zVelocity = 15f;
                }
            }
        }

        PlayerComponent pComp = player.getComponent(PlayerComponent.class);
        if (pComp.currentState == PlayerState.TACKLING || pComp.currentState == PlayerState.SLIDING) {
            for (Entity other : getEngine().getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class).get())) {
                if (other == player) continue;
                PlayerComponent otherComp = other.getComponent(PlayerComponent.class);
                if (otherComp.teamId != pComp.teamId) {
                    PositionComponent otherPos = other.getComponent(PositionComponent.class);
                    float dx = otherPos.x - playerPos.x;
                    float dy = otherPos.y - playerPos.y;
                    if (dx * dx + dy * dy < 4.0f) {
                        otherComp.hp -= 34f;
                        if (otherComp.hp <= 0) {
                            otherComp.currentState = PlayerState.UNCONSCIOUS;
                        }

                        // Foul logic
                        if (Math.random() < 0.2) { // 20% chance of foul on successful tackle
                            // In a full implementation, we should also trigger MatchState.FOUL, FREE_KICK, or PENALTY
                            // To prevent softlocking, we just log the card/ejection for now and keep play moving,
                            // or properly implement the state machine transition in a complete rule set.
                            if (Math.random() < 0.2) { // 20% of fouls are red cards
                                pComp.redCard = true;
                                pComp.currentState = PlayerState.EJECTED;
                            } else {
                                pComp.yellowCards++;
                                if (pComp.yellowCards >= 2) {
                                    pComp.redCard = true;
                                    pComp.currentState = PlayerState.EJECTED;
                                }
                            }
                        }

                        if (ballComponent.owner == other) {
                            ballComponent.owner = null;
                        }
                        if (pComp.currentState != PlayerState.EJECTED) {
                            pComp.currentState = PlayerState.IDLE;
                        }
                        break;
                    }
                }
            }
        }

        // If this player has the ball, make the ball follow the player
        if (ballComponent.owner == player) {
            ballPos.x = playerPos.x;
            ballPos.y = playerPos.y;
            VelocityComponent ballVelocityComponent = ball.getComponent(VelocityComponent.class);
            if (ballVelocityComponent != null) {
                ballVelocityComponent.x = 0;
                ballVelocityComponent.y = 0;
                ballComponent.zVelocity = 0;
                ballComponent.z = 0;
            }
        }
    }
}
