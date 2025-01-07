package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TileManager {
    GamePanel gp;

    /**
     * A hashmap of all the tiles in the world
     */
    HashMap<String, ArrayList<Tile>> tiles;

    /**
     * The position of a tile in the tile array
     */
    public int[][] mapTileNum;

    /**
     *
     * @param gp
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        TileLoader tileLoader = new TileLoader();
        tiles = tileLoader.Tiles;
        loadMap("/maps/world01.txt");
    }

    /**
     * Loads a tile map from a text file
     * @param mapPath The filepath of the map being loaded
     */
    private void loadMap(String mapPath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();

                while(col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the tile map onto the camera
     * Only loads visible tiles
     * Centers the player to the screen
     * @param g2
     */
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            if (tiles.get("dungeon").get(tileNum) != null) {
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - (int) gp.player.worldX + gp.player.screenX; //screenX is an offset accounting for the centering of the player
                int screenY = worldY - (int) gp.player.worldY + gp.player.screenY; //screenY is an offset accounting for the centering of the player

                //Draw tile if in bounds of screen
                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - (2 * gp.tileSize) < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - (2 * gp.tileSize) < gp.player.worldY + gp.player.screenY) {

                    g2.drawImage(tiles.get("dungeon").get(tileNum).image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}