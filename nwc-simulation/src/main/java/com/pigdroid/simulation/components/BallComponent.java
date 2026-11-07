package com.pigdroid.simulation.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class BallComponent implements Component {
    public Entity owner; // Null if no one has the ball
    public float z; // Height of the ball (for lobs/supershots)
    public float zVelocity;
    public Entity lastTouchedBy;

    public BallComponent() {
        this.owner = null;
        this.z = 0;
        this.zVelocity = 0;
        this.lastTouchedBy = null;
    }
}
