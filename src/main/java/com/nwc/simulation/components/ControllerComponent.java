package com.nwc.simulation.components;

import com.badlogic.ashley.core.Component;
import com.nwc.simulation.controllers.PlayerController;

public class ControllerComponent implements Component {
    public PlayerController controller;

    public ControllerComponent(PlayerController controller) {
        this.controller = controller;
    }
}
