package main;

import javax.swing.*;

public class Main {

    public static void main (String[] args) {
        JFrame window;
        window = createWindow();
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }

    private static JFrame createWindow () {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Tower Defense");
        return window;
    }
}
