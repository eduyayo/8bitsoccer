package com.pigdroid.simulation.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.HazardComponent;
import com.pigdroid.simulation.components.PlayerComponent;
import com.pigdroid.simulation.components.PositionComponent;
import com.pigdroid.simulation.components.VelocityComponent;
import com.pigdroid.simulation.fsm.PlayerState;

public class HazardSystem extends IteratingSystem {
    private ImmutableArray<Entity> hazards;

    public HazardSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).one(PlayerComponent.class, BallComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        hazards = engine.getEntitiesFor(Family.all(HazardComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        VelocityComponent vel = entity.getComponent(VelocityComponent.class);
        PlayerComponent player = entity.getComponent(PlayerComponent.class);
        BallComponent ball = entity.getComponent(BallComponent.class);

        for (Entity hazardEntity : hazards) {
            PositionComponent hazardPos = hazardEntity.getComponent(PositionComponent.class);
            HazardComponent hazard = hazardEntity.getComponent(HazardComponent.class);

            float dx = pos.x - hazardPos.x;
            float dy = pos.y - hazardPos.y;
            float distanceSq = dx * dx + dy * dy;

            if (distanceSq <= hazard.radius * hazard.radius) {
                // Collision with hazard
                if (player != null) {
                    float speed = (float) Math.sqrt(vel.x * vel.x + vel.y * vel.y);

                    if (hazard.type == HazardComponent.Type.ROCK) {
                        player.currentState = PlayerState.DOWNED;
                        vel.x = 0;
                        vel.y = 0;
                    } else if (hazard.type == HazardComponent.Type.PUDDLE) {
                        // Slow down player speed. If moving fast, spin out and go DOWNED
                        if (speed > 4.0f) { // Threshold for "moving fast"
                            player.currentState = PlayerState.DOWNED;
                            vel.x = 0;
                            vel.y = 0;
                        } else {
                            // Slow down
                            vel.x *= 0.5f;
                            vel.y *= 0.5f;
                        }
                    }
                } else if (ball != null) {
                    if (hazard.type == HazardComponent.Type.PUDDLE) {
                        // Drastically cut ball velocity
                        vel.x *= 0.2f;
                        vel.y *= 0.2f;
                    }
                }
            }
        }
    }
}
