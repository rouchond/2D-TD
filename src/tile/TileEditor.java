package tile;

import sprites.SpriteUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TileEditor {

    TilePanel tp;

    /**
     * A hashmap of all the tiles in the world
     */
    HashMap<String, ArrayList<Tile>> tiles;

    /**
     * The position of a tile in the tile array
     */
    public Tile [][] mapTileNum;

    public TileEditor(TilePanel tp) {
        this.tp = tp;
        mapTileNum = new Tile[tp.maxWorldCol][tp.maxWorldRow];
        TileLoader tileLoader = new TileLoader();
        tiles = tileLoader.Tiles;
    }


    /**
     * Draws the tile map onto the camera
     * Only loads visible tiles
     * Centers the player to the screen
     * @param g2
     */
    public void drawPlaced(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < tp.maxWorldCol && worldRow < tp.maxWorldRow) {
            if (mapTileNum[worldCol][worldRow] != null) {
                String tileSet = mapTileNum[worldCol][worldRow].tileSet;
                int tileNum = mapTileNum[worldCol][worldRow].tileIndex;

                // Convert world position to screen position, accounting for camera
                int worldX = worldCol * tp.tileSize;
                int worldY = worldRow * tp.tileSize;
                int screenX = worldX - (int) tp.xPos;  // Subtract camera position
                int screenY = worldY - (int) tp.yPos;  // Subtract camera position

                // Draw tile if in bounds of screen
                if (screenX + tp.tileSize > 0 &&
                        screenX < tp.screenWidth &&
                        screenY + tp.tileSize > 0 &&
                        screenY < tp.screenHeight) {

                    g2.drawImage(tiles.get(tileSet).get(tileNum).image, screenX, screenY, tp.tileSize, tp.tileSize, null);
                }
            }

            worldCol++;

            if (worldCol == tp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    /**
     * Draws the tile that would be placed if you were to click in a lower opacity
     * @param g2 Graphics2D object
     * @param tileImg Image of the tile we want to place
     * @param tileCol Current Column the mouse is in
     * @param tileRow Current Row the mouse is in
     */
    public void drawMouse(Graphics2D g2, BufferedImage tileImg, int tileCol, int tileRow) {
        BufferedImage img = SpriteUtils.setImageOpacity(tileImg, 0.5f);

        // Convert world position to screen position by subtracting camera position
        int screenX = (tileCol * tp.tileSize) - (int)tp.xPos;
        int screenY = (tileRow * tp.tileSize) - (int)tp.yPos;

        g2.drawImage(img, screenX, screenY, tp.tileSize, tp.tileSize, null);
    }



    /**
     * Saves the current state of the TileEditor as a map
     */
    public void saveMap() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("res/maps/generatedMap.txt"))) {

            // Go through entire map
            int worldCol = 0;
            int worldRow = 0;

            while (worldCol < tp.maxWorldCol && worldRow < tp.maxWorldRow) {

                // Save tile if it's not null
                if (mapTileNum[worldCol][worldRow] != null) {
                    String tileSet = mapTileNum[worldCol][worldRow].tileSet;
                    int tileNum = mapTileNum[worldCol][worldRow].tileIndex;

                    String line = tileSet + "," + tileNum + "," + worldCol + "," + worldRow;
                    writer.write(line);
                    writer.newLine();
                    }


                worldCol++;

                if (worldCol == tp.maxWorldCol) {
                    worldCol = 0;
                    worldRow++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            }
    }


}

