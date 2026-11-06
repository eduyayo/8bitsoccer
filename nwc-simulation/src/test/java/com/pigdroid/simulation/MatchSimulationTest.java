package com.pigdroid.simulation;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.PlayerComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MatchSimulationTest {

    @Test
    public void testEngineInitialization() {
        MatchSimulation simulation = new MatchSimulation();
        Engine engine = simulation.getEngine();

        assertNotNull(engine);

        // 1 ball + 12 players
        assertEquals(13, engine.getEntities().size());

        long balls = 0;
        long players = 0;

        for (Entity e : engine.getEntities()) {
            if (e.getComponent(BallComponent.class) != null) balls++;
            if (e.getComponent(PlayerComponent.class) != null) players++;
        }

        assertEquals(1, balls);
        assertEquals(12, players);
    }

    @Test
    public void testSimulationTick() {
        MatchSimulation simulation = new MatchSimulation();
        // Should run without crashing
        simulation.update(1.0f / 60.0f);
        simulation.update(1.0f / 60.0f);
    }
}
