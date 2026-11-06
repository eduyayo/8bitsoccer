package com.pigdroid.simulation.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pigdroid.simulation.components.PositionComponent;
import com.pigdroid.simulation.components.VelocityComponent;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.config.MatchConfig;
import com.pigdroid.simulation.config.FieldType;

public class MovementSystem extends IteratingSystem {
    private MatchConfig matchConfig;

    public MovementSystem(MatchConfig matchConfig) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
        this.matchConfig = matchConfig;
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

                // Apply friction to the ball when rolling on the ground
                if (ball.owner == null) {
                    float friction = 2.0f; // Default grass friction
                    if (matchConfig.fieldType == FieldType.SAND) {
                        friction = 5.0f; // High friction
                    } else if (matchConfig.fieldType == FieldType.ICE) {
                        friction = 0.5f; // Low friction
                    }

                    float speed = (float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
                    if (speed > 0) {
                        float drop = speed * friction * deltaTime;
                        float newSpeed = Math.max(0, speed - drop);
                        velocity.x *= newSpeed / speed;
                        velocity.y *= newSpeed / speed;
                    }
                }
            }
        }
    }
}
