package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Objects;

public class TileEditor {

    TilePanel tp;

    /**
     * An array of all the tiles in the world
     */
    public Tile[] tile;

    /**
     * The position of a tile in the tile array
     */
    public int[][] mapTileNum;

    public TileEditor(TilePanel tp) {
        this.tp = tp;
        tile = new Tile[10];
        mapTileNum = new int[tp.maxWorldCol][tp.maxWorldRow];
        getTileImage();
    }

    /**
     * Load all the tile sprites
     */
    public void getTileImage() {
        try{
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dungeon01.png")));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dungeon02.png")));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dungeon03.png")));
            tile[3].collision = true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
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
            int tileNum = mapTileNum[worldCol][worldRow];
            if (tile[tileNum] != null) {
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

                    g2.drawImage(tile[tileNum].image, screenX, screenY, tp.tileSize, tp.tileSize, null);
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
        BufferedImage img = setImageOpacity(tileImg, 0.5f);

        // Convert world position to screen position by subtracting camera position
        int screenX = (tileCol * tp.tileSize) - (int)tp.xPos;
        int screenY = (tileRow * tp.tileSize) - (int)tp.yPos;

        g2.drawImage(img, screenX, screenY, tp.tileSize, tp.tileSize, null);
    }



    /**
     * Saves the current state of the TileEditor as a map
     */
    public void saveMap() {

    }

    /**
     * Sets the opacity of the image
     * @param originalImage image that we're changing the opacity of
     * @param opacity the opacity we are setting the image to
     */
    private BufferedImage setImageOpacity(BufferedImage originalImage, float opacity) {
        // Create a new BufferedImage with transparency support
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Get graphics context for the new image
        Graphics2D g2d = newImage.createGraphics();

        // Set up alpha composite
        AlphaComposite alphaChannel = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                opacity
        );
        g2d.setComposite(alphaChannel);

        // Draw the original image with new opacity
        g2d.drawImage(originalImage, 0, 0, null);

        // Clean up
        g2d.dispose();

        return newImage;
    }
}

