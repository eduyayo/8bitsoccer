package com.nwc.simulation;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.nwc.simulation.components.*;
import com.nwc.simulation.controllers.AIController;
import com.nwc.simulation.fsm.MatchStateMachine;
import com.nwc.simulation.fsm.PlayerStateMachine;
import com.nwc.simulation.systems.*;

public class MatchSimulation {
    private Engine engine;
    private MatchSystem matchSystem;
    private ControllerSystem controllerSystem;
    private InteractionSystem interactionSystem;

    public MatchSimulation() {
        engine = new Engine();
        initializeSystems();
        initializeEntities();
    }

    private void initializeSystems() {
        MatchStateMachine matchStateMachine = new MatchStateMachine();
        PlayerStateMachine playerStateMachine = new PlayerStateMachine();

        controllerSystem = new ControllerSystem();
        interactionSystem = new InteractionSystem();

        engine.addSystem(controllerSystem);
        engine.addSystem(new PlayerBehaviorSystem(playerStateMachine));
        engine.addSystem(new MovementSystem());
        engine.addSystem(interactionSystem);

        matchSystem = new MatchSystem(matchStateMachine);
        engine.addSystem(matchSystem);
    }

    private void initializeEntities() {
        // Create ball
        Entity ball = new Entity();
        ball.add(new PositionComponent(0, 0));
        ball.add(new VelocityComponent(0, 0));
        ball.add(new BallComponent());
        engine.addEntity(ball);

        controllerSystem.setBall(ball);
        interactionSystem.setBall(ball);

        // Create players (e.g., 6 per team for NWC)
        for (int team = 0; team < 2; team++) {
            for (int i = 0; i < 6; i++) {
                Entity player = new Entity();
                float startX = (team == 0) ? -20 - i * 10 : 20 + i * 10;
                float startY = -30 + i * 10;

                player.add(new PositionComponent(startX, startY));
                player.add(new VelocityComponent(0, 0));
                player.add(new PlayerComponent(team, i));
                player.add(new ControllerComponent(new AIController()));

                engine.addEntity(player);
            }
        }
    }

    public void update(float deltaTime) {
        engine.update(deltaTime);
    }

    public Engine getEngine() {
        return engine;
    }
}
