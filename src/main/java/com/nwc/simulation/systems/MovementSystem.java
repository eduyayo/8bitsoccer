package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nwc.simulation.components.PositionComponent;
import com.nwc.simulation.components.VelocityComponent;
import com.nwc.simulation.components.BallComponent;

public class MovementSystem extends IteratingSystem {
    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        BallComponent ball = entity.getComponent(BallComponent.class);
        if (ball != null) {
            ball.z += ball.zVelocity * deltaTime;
            if (ball.z > 0) {
                ball.zVelocity -= 9.8f * deltaTime; // Gravity
            } else {
                ball.z = 0;
                ball.zVelocity = 0;
            }
        }
    }
}
