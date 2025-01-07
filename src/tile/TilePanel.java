package tile;

import main.KeyHandler;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePanel extends JPanel implements Runnable{

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
    TileEditor tileE = new TileEditor(this);

    // User Positions

    /**
     * X Position of the user
     */
    public int xPos = 0;

    /**
     * Y position of the user
     */
    public int yPos = 0;

    /**
     * X pos of the screen
     */
    public final int screenX = screenWidth/2 - (tileSize);

    /**
     * Y pos of the screen
     */
    public final int screenY = screenHeight/2 - (tileSize);

    /**
     * Current column the mouse is in relative to the tile grid
     */
    public int currentTileCol = 0;

    /**
     * Current column the mouse is in relative to the tile grid
     */
    public int currentTileRow = 0;

    // Tile Placements

    /**
     * The tileSet array being used
     */
    private String tileSet = "dungeon";

    /**
     * The index of the tile in our specified tileSet
     */
    private int tileIndex = 1;

    /**
     * The index of the tile prior to erasing
     */
    private int erasingIndex = 1;

    /**
     * An empty black game window that can listen for key inputs
     * Default Dimensions: 768 Pixels x 576 Pixels
     */
    public TilePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
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
        setErasing();
        changeTile();
        moveCamera();
        updateMousePosition();
        placeTile();
        saveMap();
        keyH.update();
    }

    private void moveCamera () {
        if (keyH.rightPressed) {
            xPos = Math.min(xPos + 5, (maxWorldCol * tileSize) - screenWidth);
        } else if (keyH.leftPressed) {
            xPos = Math.max(0, xPos - 5);
        }

        if (keyH.downPressed) {
            yPos = Math.min(yPos + 5, (maxWorldRow * tileSize) - screenWidth);
        } else if (keyH.upPressed) {
            yPos = Math.max(0, yPos - 5);
        }
    }

    /**
     * When left-clicking either erase the tile or place the new tile
     */
    private void placeTile () {
        if (keyH.mousePressed) {
            if (tileIndex != 0) {
                Tile tile = new Tile();
                tile.tileIndex = tileIndex;
                tile.tileSet = tileSet;
                tile.image = tileE.tiles.get(tileSet).get(tileIndex).image;
                tileE.mapTileNum[currentTileCol][currentTileRow] = tile;
            } else {
                tileE.mapTileNum[currentTileCol][currentTileRow] = null;
            }
        }

    }

    private void saveMap () {
        if (keyH.mPressed && !keyH.previousMPressed) {
            tileE.saveMap();
        }
    }

    /**
     * Updates the current mouse position relative to the tile grid
     */
    private void updateMousePosition() {
        // Get global mouse position
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        // Convert to component coordinates
        SwingUtilities.convertPointFromScreen(mousePoint, this);

        // Convert to tile coordinates using your previous method
        int[] tilePos = getMouseTilePosition(mousePoint.x, mousePoint.y);
        currentTileCol = tilePos[0];
        currentTileRow = tilePos[1];
    }

    /**
     * Converts screen mouse coordinates to tile grid coordinates
     * @param mouseX The x-coordinate of the mouse on screen
     * @param mouseY The y-coordinate of the mouse on screen
     * @return int[] containing [tileCol, tileRow]
     */
    private int[] getMouseTilePosition(int mouseX, int mouseY) {
        // Add camera position to get world coordinates
        int worldX = mouseX + xPos;
        int worldY = mouseY + yPos;

        // Convert to tile coordinates
        int tileCol = worldX / tileSize;
        int tileRow = worldY / tileSize;

        // Ensure we stay within bounds
        tileCol = Math.max(0, Math.min(tileCol, maxWorldCol - 1));
        tileRow = Math.max(0, Math.min(tileRow, maxWorldRow - 1));

        return new int[]{tileCol, tileRow};
    }

    /**
     * Changes the tile being placed.
     */
    private void changeTile () {
        if (keyH.upArrowPressed && !keyH.previousUpArrowPressed) {
            if (tileIndex == tileE.tiles.get(tileSet).size() - 1) {
                tileIndex = 1;
            } else {
                tileIndex++;
            }
        } else if (keyH.downArrowPressed && !keyH.previousDownArrowPressed) {
            if (tileIndex == 1) {
                tileIndex = tileE.tiles.get(tileSet).size() - 1;
            } else {
                tileIndex--;
            }
        }
    }

    /**
     *
     */
    private void setErasing () {
        if (keyH.ePressed) {
            if (tileIndex != 0) {
                erasingIndex = tileIndex;
                tileIndex = 0;
            }
            else {
                tileIndex = erasingIndex;
            }
        }
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
        tileE.drawPlaced(g2);
        if (tileIndex != 0) {
            tileE.drawMouse(g2, tileE.tiles.get(tileSet).get(tileIndex).image, currentTileCol, currentTileRow);
        }

    }
}
