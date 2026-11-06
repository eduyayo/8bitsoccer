package com.pigdroid.simulation.components;

import com.badlogic.ashley.core.Component;
import com.pigdroid.simulation.controllers.PlayerController;

public class ControllerComponent implements Component {
    public PlayerController controller;

    public ControllerComponent(PlayerController controller) {
        this.controller = controller;
    }
}
