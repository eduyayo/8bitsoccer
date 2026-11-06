package com.nwc.simulation;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.nwc.simulation.components.*;
import com.nwc.simulation.controllers.AIController;
import com.nwc.simulation.fsm.MatchStateMachine;
import com.nwc.simulation.fsm.PlayerStateMachine;
import com.nwc.simulation.systems.*;
import com.nwc.simulation.config.MatchConfig;
import com.nwc.simulation.config.TeamConfig;
import com.nwc.simulation.config.PlayerProfile;
import java.util.List;

import java.util.Random;

public class MatchSimulation {
    private Engine engine;
    private MatchSystem matchSystem;
    private ControllerSystem controllerSystem;
    private InteractionSystem interactionSystem;
    private MatchConfig matchConfig;
    private Random random;

    public MatchSimulation() {
        this(MatchConfig.createDefault());
    }

    public MatchSimulation(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
        this.random = new Random(matchConfig.seed);
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
        engine.addSystem(new PlayerBehaviorSystem(playerStateMachine, matchConfig));
        engine.addSystem(new MovementSystem(matchConfig));
        engine.addSystem(interactionSystem);
        engine.addSystem(new HazardSystem());

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

        // Create players based on config
        createTeamEntities(0, matchConfig.team1.players);
        createTeamEntities(1, matchConfig.team2.players);

        // Create hazards based on config
        if (matchConfig.fieldType == com.nwc.simulation.config.FieldType.DIRT) {
            createHazards(HazardComponent.Type.ROCK, 5, 2.0f);
        }
        if (matchConfig.weatherCondition == com.nwc.simulation.config.WeatherCondition.RAIN) {
            createHazards(HazardComponent.Type.PUDDLE, 10, 4.0f);
        }
    }

    private void createHazards(HazardComponent.Type type, int count, float radius) {
        for (int i = 0; i < count; i++) {
            Entity hazard = new Entity();
            // Randomly scatter around the pitch. Assume pitch bounds roughly -50 to 50 for X, -30 to 30 for Y.
            float x = (float) (random.nextDouble() * 100 - 50);
            float y = (float) (random.nextDouble() * 60 - 30);
            hazard.add(new PositionComponent(x, y));
            hazard.add(new HazardComponent(type, radius));
            engine.addEntity(hazard);
        }
    }

    private void createTeamEntities(int teamId, List<PlayerProfile> players) {
        for (int i = 0; i < players.size(); i++) {
            PlayerProfile profile = players.get(i);
            Entity player = new Entity();
            float startX = (teamId == 0) ? -20 - i * 10 : 20 + i * 10;
            float startY = -30 + i * 10;

            player.add(new PositionComponent(startX, startY));
            player.add(new VelocityComponent(0, 0));
            player.add(new PlayerComponent(teamId, i, profile));
            player.add(new ControllerComponent(new AIController()));

            engine.addEntity(player);
        }
    }

    public void update(float deltaTime) {
        engine.update(deltaTime);
    }

    public Engine getEngine() {
        return engine;
    }
}
