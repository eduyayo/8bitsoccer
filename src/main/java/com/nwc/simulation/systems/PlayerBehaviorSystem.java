package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nwc.simulation.components.ControllerComponent;
import com.nwc.simulation.components.PlayerComponent;
import com.nwc.simulation.components.VelocityComponent;
import com.nwc.simulation.controllers.PlayerController;
import com.nwc.simulation.fsm.PlayerStateMachine;
import com.nwc.simulation.config.MatchConfig;
import com.nwc.simulation.config.FieldType;

public class PlayerBehaviorSystem extends IteratingSystem {
    private PlayerStateMachine stateMachine;
    private MatchConfig matchConfig;

    public PlayerBehaviorSystem(PlayerStateMachine stateMachine, MatchConfig matchConfig) {
        super(Family.all(PlayerComponent.class, ControllerComponent.class, VelocityComponent.class).get());
        this.stateMachine = stateMachine;
        this.matchConfig = matchConfig;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        ControllerComponent controllerComponent = entity.getComponent(ControllerComponent.class);
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);

        PlayerController controller = controllerComponent.controller;

        // Apply controller inputs to physics/state
        float topSpeed = playerComponent.profile != null ? playerComponent.profile.physical.topSpeed : playerComponent.speed;

        if (matchConfig.fieldType == FieldType.SAND) {
            topSpeed *= 0.5f; // Cap top speed to 50% on sand
        }

        float targetVx = controller.getMoveX() * topSpeed;
        float targetVy = controller.getMoveY() * topSpeed;

        float accelerationRate = 10f; // Default acceleration
        if (matchConfig.fieldType == FieldType.ICE) {
            accelerationRate = 1f; // Extremely low traction
        }

        // If there's no input and not on ice, decelerate quickly
        if (controller.getMoveX() == 0 && controller.getMoveY() == 0 && matchConfig.fieldType != FieldType.ICE) {
            accelerationRate = 20f;
        }

        // Smoothly interpolate current velocity towards target velocity
        velocityComponent.x += (targetVx - velocityComponent.x) * accelerationRate * deltaTime;
        velocityComponent.y += (targetVy - velocityComponent.y) * accelerationRate * deltaTime;

        // FSM update handles state transitions
        stateMachine.update(entity, deltaTime);
    }
}
