package com.pigdroid.watchable.viewer;

import com.pigdroid.simulation.MatchSimulation;
import com.pigdroid.simulation.config.MatchConfig;

import javax.swing.*;
import java.awt.*;

public class WatchableViewerFrame extends JFrame {
    private MatchSimulation simulation;
    private WatchableMatchPanel panel;

    public WatchableViewerFrame(MatchConfig config) {
        super("Match Watcher");
        this.simulation = new MatchSimulation(config);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        this.panel = new WatchableMatchPanel(simulation);
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        Timer timer = new Timer(16, e -> {
            simulation.update(0.016f);
            panel.repaint();
        });
        timer.start();
    }
}
