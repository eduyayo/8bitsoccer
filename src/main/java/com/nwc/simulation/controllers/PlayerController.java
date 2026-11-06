package com.nwc.simulation.controllers;

public interface PlayerController {
    float getMoveX();
    float getMoveY();
    boolean isPassPressed();
    boolean isShootPressed();
    boolean isTacklePressed();
    void update(float deltaTime, com.badlogic.ashley.core.Entity player, com.badlogic.ashley.core.Entity ball);
}
