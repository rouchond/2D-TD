package main;

import player.Player;
import tile.Tile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    /**
     * The size of a tile
     */
    final int originalTileSize = 16;

    /**
     * Scaling factor of the game
     */
    final int scale = 3;

    /**
     *Size of the tile after applying scaling
     */
    public final int tileSize = originalTileSize * scale;

    /**
     * Width of the screen (unit is per tile)
     */
    public final int maxScreenCol = 16;

    /**
     * Width of the screen (unit is per tile)
     */
    public final int maxScreenRow = 12;

    /**
     * Width of the screen scaled to tileSize
     */
    public final int screenWidth = tileSize * maxScreenCol;

    /**
     * Height of the screen scaled to tileSize
     */
    public final int screenHeight = tileSize * maxScreenRow;

    // World Settings

    /**
     * The width of the world (unit is per tile)
     */
    public final int maxWorldCol = 20;

    /**
     * The height of the world (unit is per tile)
     */
    public final int maxWorldRow = 20;

    // Time


    /**
     * FPS cap of the game
     */
    int fps = 60;

    /**
     * Current FPS of the user
     */
    public int currentFPS;

    /**
     * Elapsed time since last update
     */
    public double deltaTime;

    // System

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    TileManager tileM = new TileManager(this);

    // Entities
    public Player player = new Player(this, keyH);

    /**
     * An empty black game window that can listen for key inputs
     * Default Dimensions: 768 Pixels x 576 Pixels
     */
    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    private void setTiles () {

    }

    private void setScreen () {

    }

    /**
     * Instantiating a gameThread with the panel and starting thread
     */
    public void startGameThread () {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Update the state of the game based on FPS
     */
    public void update () {
        keyH.update();
        player.update();
    }

    /**
     * When the thread is started, run is called
     * Run handles the time (FPS) in the game
     */
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / fps;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        /**
         * Check the current time, then determine how much time has passed
         * and divide it by the draw interval, then add that to delta.
         * Then update lastTime to currentTime.
         * Once delta reaches the drawInterval (1), update and repaint.
         * Then reset delta.
         */

        while (gameThread != null) {
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (deltaTime >= 1) {
                update();
                repaint();
                deltaTime--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Tile Manager
        tileM.draw(g2);

        // Player
        player.draw(g2);
    }
}
