package com.nwc.simulation.components;

import com.badlogic.ashley.core.Component;

public class HazardComponent implements Component {
    public enum Type {
        ROCK,
        PUDDLE
    }

    public Type type;
    public float radius;

    public HazardComponent(Type type, float radius) {
        this.type = type;
        this.radius = radius;
    }
}
