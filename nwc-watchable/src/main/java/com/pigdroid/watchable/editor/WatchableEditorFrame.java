package com.pigdroid.watchable.editor;

import com.pigdroid.watchable.viewer.WatchableViewerFrame;
import com.pigdroid.simulation.config.MatchConfig;
import com.pigdroid.simulation.config.PlayerProfile;
import com.pigdroid.simulation.config.TeamConfig;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WatchableEditorFrame extends JFrame {

    private MatchConfig matchConfig;
    private Random random = new Random();

    public WatchableEditorFrame() {
        super("Watchable Team Editor");
        this.matchConfig = MatchConfig.createDefault();
        randomizeConfig();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Team 1", createTeamPanel(matchConfig.team1));
        tabbedPane.addTab("Team 2", createTeamPanel(matchConfig.team2));
        add(tabbedPane, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Match");
        startButton.addActionListener(e -> {
            WatchableViewerFrame viewer = new WatchableViewerFrame(matchConfig);
            viewer.setVisible(true);
            this.setVisible(false);
        });
        add(startButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createTeamPanel(TeamConfig team) {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<PlayerProfile> listModel = new DefaultListModel<>();
        for (PlayerProfile player : team.players) {
            listModel.addElement(player);
        }

        JList<PlayerProfile> playerList = new JList<>(listModel);
        playerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PlayerProfile) {
                    setText(((PlayerProfile) value).name);
                }
                return this;
            }
        });

        JPanel editorPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        editorPanel.setBorder(BorderFactory.createTitledBorder("Attributes"));

        JTextField speedField = new JTextField();
        JTextField staminaField = new JTextField();
        JTextField tacklingField = new JTextField();
        JTextField finishingField = new JTextField();
        JTextField passingField = new JTextField();
        JTextField ballControlField = new JTextField();

        editorPanel.add(new JLabel("Top Speed:"));
        editorPanel.add(speedField);
        editorPanel.add(new JLabel("Stamina:"));
        editorPanel.add(staminaField);
        editorPanel.add(new JLabel("Tackling:"));
        editorPanel.add(tacklingField);
        editorPanel.add(new JLabel("Finishing:"));
        editorPanel.add(finishingField);
        editorPanel.add(new JLabel("Passing (Short):"));
        editorPanel.add(passingField);
        editorPanel.add(new JLabel("Ball Control:"));
        editorPanel.add(ballControlField);

        playerList.addListSelectionListener(e -> {
            PlayerProfile selected = playerList.getSelectedValue();
            if (selected != null) {
                speedField.setText(String.format("%.1f", selected.physical.topSpeed));
                staminaField.setText(String.format("%.1f", selected.physical.stamina));
                tacklingField.setText(String.format("%.1f", selected.technical.tackling));
                finishingField.setText(String.format("%.1f", selected.technical.finishing));
                passingField.setText(String.format("%.1f", selected.technical.shortPassing));
                ballControlField.setText(String.format("%.1f", selected.technical.ballControl));
            }
        });

        // Save button to persist back to object
        JButton saveBtn = new JButton("Apply Changes");
        saveBtn.addActionListener(e -> {
            PlayerProfile selected = playerList.getSelectedValue();
            if (selected != null) {
                try {
                    selected.physical.topSpeed = Float.parseFloat(speedField.getText());
                    selected.physical.stamina = Float.parseFloat(staminaField.getText());
                    selected.technical.tackling = Float.parseFloat(tacklingField.getText());
                    selected.technical.finishing = Float.parseFloat(finishingField.getText());
                    selected.technical.shortPassing = Float.parseFloat(passingField.getText());
                    selected.technical.ballControl = Float.parseFloat(ballControlField.getText());
                    JOptionPane.showMessageDialog(this, "Attributes updated for " + selected.name);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.");
                }
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(editorPanel, BorderLayout.CENTER);
        rightPanel.add(saveBtn, BorderLayout.SOUTH);

        panel.add(new JScrollPane(playerList), BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        if (!listModel.isEmpty()) {
            playerList.setSelectedIndex(0);
        }

        return panel;
    }

    private void randomizeConfig() {
        randomizeTeam(matchConfig.team1);
        randomizeTeam(matchConfig.team2);
    }

    private void randomizeTeam(TeamConfig team) {
        for (PlayerProfile profile : team.players) {
            profile.physical.topSpeed = 10f + random.nextFloat() * 80f;
            profile.physical.stamina = 10f + random.nextFloat() * 80f;
            profile.technical.tackling = 10f + random.nextFloat() * 80f;
            profile.technical.finishing = 10f + random.nextFloat() * 80f;
            profile.technical.shortPassing = 10f + random.nextFloat() * 80f;
            profile.technical.ballControl = 10f + random.nextFloat() * 80f;
            profile.mental.vision = 10f + random.nextFloat() * 80f;
            profile.mental.aggression = 10f + random.nextFloat() * 80f;
        }
    }
}
