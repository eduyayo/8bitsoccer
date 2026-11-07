package com.pigdroid.interactive.viewer;

import com.pigdroid.simulation.MatchSimulation;
import com.pigdroid.simulation.config.MatchConfig;

import javax.swing.*;
import java.awt.*;

public class MatchViewerFrame extends JFrame {
    private MatchSimulation simulation;
    private MatchPanel panel;

    public MatchViewerFrame(MatchConfig config) {
        super("Match Viewer");
        this.simulation = new MatchSimulation(config);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        this.panel = new MatchPanel(simulation);
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        Timer timer = new Timer(16, e -> {
            simulation.update(0.016f);
            panel.repaint();
        });
        timer.start();
    }
}
