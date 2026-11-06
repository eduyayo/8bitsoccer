package com.nwc.simulation.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent() {
        this(0, 0);
    }

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
