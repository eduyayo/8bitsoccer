package com.pigdroid.interactive;

import com.pigdroid.interactive.editor.TeamEditorFrame;
import javax.swing.SwingUtilities;

public class InteractiveApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeamEditorFrame frame = new TeamEditorFrame();
            frame.setVisible(true);
        });
    }
}
