package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nwc.simulation.components.ControllerComponent;
import com.nwc.simulation.components.PlayerComponent;
import com.nwc.simulation.components.VelocityComponent;
import com.nwc.simulation.controllers.PlayerController;
import com.nwc.simulation.fsm.PlayerStateMachine;

public class PlayerBehaviorSystem extends IteratingSystem {
    private PlayerStateMachine stateMachine;

    public PlayerBehaviorSystem(PlayerStateMachine stateMachine) {
        super(Family.all(PlayerComponent.class, ControllerComponent.class, VelocityComponent.class).get());
        this.stateMachine = stateMachine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        ControllerComponent controllerComponent = entity.getComponent(ControllerComponent.class);
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);

        PlayerController controller = controllerComponent.controller;

        // Apply controller inputs to physics/state
        float topSpeed = playerComponent.profile != null ? playerComponent.profile.physical.topSpeed : playerComponent.speed;
        velocityComponent.x = controller.getMoveX() * topSpeed;
        velocityComponent.y = controller.getMoveY() * topSpeed;

        // FSM update handles state transitions
        stateMachine.update(entity, deltaTime);
    }
}
