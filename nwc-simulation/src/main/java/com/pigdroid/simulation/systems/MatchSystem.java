package com.pigdroid.simulation.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.PositionComponent;
import com.pigdroid.simulation.fsm.MatchStateMachine;
import com.pigdroid.simulation.fsm.MatchState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchSystem extends EntitySystem {
    private static final Logger logger = LoggerFactory.getLogger(MatchSystem.class);
    private MatchStateMachine stateMachine;
    private int team0Score = 0;
    private int team1Score = 0;

    public MatchSystem(MatchStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void update(float deltaTime) {
        stateMachine.update(deltaTime);

        if (stateMachine.getCurrentState() == MatchState.PLAYING) {
            Engine engine = getEngine();
            if (engine != null) {
                for (Entity ball : engine.getEntitiesFor(Family.all(BallComponent.class, PositionComponent.class).get())) {
                    PositionComponent pos = ball.getComponent(PositionComponent.class);

                    if (pos.y > 30 || pos.y < -30) {
                        stateMachine.changeState(MatchState.THROW_IN);
                        // Resume play automatically to prevent softlock in this MVP
                        stateMachine.changeState(MatchState.PLAYING);
                    } else if (pos.x > 50 || pos.x < -50) {
                        if (pos.y > -5 && pos.y < 5) {
                            // Goal scored handled elsewhere or can be handled here.
                        } else {
                            stateMachine.changeState(MatchState.CORNER_KICK);
                            // Resume play automatically to prevent softlock in this MVP
                            stateMachine.changeState(MatchState.PLAYING);
                        }
                    }
                }
            }
        }
    }

    public void teamScored(int teamId) {
        if (teamId == 0) team0Score++;
        else team1Score++;

        logger.info("Goal! Score is now Team 0: {} - Team 1: {}", team0Score, team1Score);
        stateMachine.changeState(com.pigdroid.simulation.fsm.MatchState.GOAL_SCORED);
    }

    public int getTeam0Score() { return team0Score; }
    public int getTeam1Score() { return team1Score; }
}
