package com.pigdroid.watchable;

import com.pigdroid.watchable.editor.WatchableEditorFrame;
import javax.swing.SwingUtilities;

public class WatchableApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WatchableEditorFrame frame = new WatchableEditorFrame();
            frame.setVisible(true);
        });
    }
}
