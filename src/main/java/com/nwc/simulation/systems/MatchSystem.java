package com.nwc.simulation.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.nwc.simulation.fsm.MatchStateMachine;
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
        // Match specific rules, boundary checks for goals, etc would go here
    }

    public void teamScored(int teamId) {
        if (teamId == 0) team0Score++;
        else team1Score++;

        logger.info("Goal! Score is now Team 0: {} - Team 1: {}", team0Score, team1Score);
        stateMachine.changeState(com.nwc.simulation.fsm.MatchState.GOAL_SCORED);
    }

    public int getTeam0Score() { return team0Score; }
    public int getTeam1Score() { return team1Score; }
}
