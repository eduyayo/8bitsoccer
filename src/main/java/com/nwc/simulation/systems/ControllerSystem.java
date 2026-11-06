package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nwc.simulation.components.BallComponent;
import com.nwc.simulation.components.ControllerComponent;

public class ControllerSystem extends IteratingSystem {
    private Entity ball;

    public ControllerSystem() {
        super(Family.all(ControllerComponent.class).get());
    }

    public void setBall(Entity ball) {
        this.ball = ball;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControllerComponent controllerComp = entity.getComponent(ControllerComponent.class);
        if (controllerComp != null && controllerComp.controller != null) {
            controllerComp.controller.update(deltaTime, entity, ball);
        }
    }
}
