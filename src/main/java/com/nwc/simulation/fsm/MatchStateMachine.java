package com.nwc.simulation.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(MatchStateMachine.class);
    private MatchState currentState;
    private float stateTime;

    public MatchStateMachine() {
        this.currentState = MatchState.KICKOFF;
        this.stateTime = 0;
    }

    public MatchState getCurrentState() {
        return currentState;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        MatchState previousState = currentState;

        switch (currentState) {
            case KICKOFF:
                // Transition to playing after a brief delay or when ball is touched
                if (stateTime > 3.0f) {
                    changeState(MatchState.PLAYING);
                }
                break;
            case PLAYING:
                break;
            case GOAL_SCORED:
                // Transition back to kickoff after goal celebration
                if (stateTime > 5.0f) {
                    changeState(MatchState.KICKOFF);
                }
                break;
            case HALF_TIME:
            case GAME_OVER:
                break;
        }
    }

    public void changeState(MatchState newState) {
        if (this.currentState != newState) {
            logger.info("Match state changed from {} to {}", this.currentState, newState);
            this.currentState = newState;
            this.stateTime = 0;
        }
    }
}
