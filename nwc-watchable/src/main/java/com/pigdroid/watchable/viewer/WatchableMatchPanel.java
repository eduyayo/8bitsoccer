package com.pigdroid.watchable.viewer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.pigdroid.simulation.MatchSimulation;
import com.pigdroid.simulation.components.BallComponent;
import com.pigdroid.simulation.components.PlayerComponent;
import com.pigdroid.simulation.components.PositionComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WatchableMatchPanel extends JPanel {
    private MatchSimulation simulation;
    private BufferedImage backgroundImage;

    // Simple 8x8 pixel sprite for player
    private static final int[][] PLAYER_SPRITE = {
        {0,0,1,1,1,1,0,0},
        {0,1,1,1,1,1,1,0},
        {0,1,0,1,1,0,1,0},
        {0,1,1,1,1,1,1,0},
        {0,0,1,1,1,1,0,0},
        {0,1,1,0,0,1,1,0},
        {1,1,1,0,0,1,1,1},
        {1,1,0,0,0,0,1,1}
    };

    public WatchableMatchPanel(MatchSimulation simulation) {
        this.simulation = simulation;
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/field.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        if (simulation != null) {
            Engine engine = simulation.getEngine();
            // Assuming X ranges from -50 to 50, map to 0 to width
            // Assuming Y ranges from -30 to 30, map to 0 to height
            float simWidth = 100f; // -50 to 50
            float simHeight = 60f; // -30 to 30

            for (Entity entity : engine.getEntitiesFor(Family.all(PositionComponent.class).get())) {
                PositionComponent pos = entity.getComponent(PositionComponent.class);

                int mappedX = (int) (((pos.x + 50) / simWidth) * getWidth());
                int mappedY = (int) (((pos.y + 30) / simHeight) * getHeight());

                PlayerComponent player = entity.getComponent(PlayerComponent.class);
                if (player != null) {
                    g.setColor(player.teamId == 0 ? Color.RED : Color.BLUE);

                    // Draw 8x8 pixel sprite centered at mappedX, mappedY
                    int pixelSize = 2; // Scale the sprite by a factor
                    int startX = mappedX - (4 * pixelSize);
                    int startY = mappedY - (4 * pixelSize);

                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {
                            if (PLAYER_SPRITE[y][x] == 1) {
                                g.fillRect(startX + (x * pixelSize), startY + (y * pixelSize), pixelSize, pixelSize);
                            }
                        }
                    }

                    g.setColor(Color.WHITE);
                    // Draw state over head
                    g.drawString(player.currentState.name(), mappedX - 15, mappedY - 15);
                }

                BallComponent ball = entity.getComponent(BallComponent.class);
                if (ball != null) {
                    g.setColor(Color.WHITE);
                    g.fillOval(mappedX - 3, mappedY - 3, 6, 6);
                }
            }
        }
    }
}
