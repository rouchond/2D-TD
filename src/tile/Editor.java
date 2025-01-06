package tile;

import main.GamePanel;

import javax.swing.*;

public class Editor {

    public static void main (String[] args) {
        JFrame window;
        window = createWindow();

        TilePanel tilePanel = new TilePanel();
        window.add(tilePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        tilePanel.startGameThread();
    }

    private static JFrame createWindow () {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Tower Defense");
        return window;
    }

}
