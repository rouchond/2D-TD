package tile;

import main.GamePanel;
import main.KeyHandler;
import tower.Tower;
import tower.TowerPlacer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    KeyHandler keyH;

    /**
     * A hashmap of all the possible tiles
     */
    HashMap<String, ArrayList<Tile>> tiles;

    /**
     * Temp tower
     */
    public Tower tempTower = new Tower();

    /**
     * A hashmap of all the possible towers
     */
    HashMap<String, ArrayList<Tower>> towers;

    /**
     * An array of all possible tower locations
     */
    public ArrayList<TowerPlacer> towerLocations = new ArrayList<>();

    /**
     * An array of all towers to remove
     */
    public ArrayList<TowerPlacer> removeTowerQueue = new ArrayList<>();

    /**
     * The position of a tile in the tile array
     */
    public Tile[][] mapTileNum;

    /**
     *
     * @param gp
     */
    public TileManager(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        mapTileNum = new Tile[gp.maxWorldCol][gp.maxWorldRow];

        TileLoader tileLoader = new TileLoader();
        tiles = tileLoader.Tiles;

        // change later
        try {
            tempTower.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/tower/dungeon-turret-01.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadMap("res/maps/world.txt");
    }

    /**
     * Loads a tile map from a text file
     * @param mapPath The filepath of the map being loaded
     */
    private void loadMap(String mapPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(mapPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma
                String[] values = line.split(",");

                // Create tile/towerPlacer from the predefined properties
                if (Integer.parseInt(values[1]) == 16 && Objects.equals(values[0], "dungeon")) {
                    TowerPlacer tile = new TowerPlacer(Integer.parseInt(values[2]), Integer.parseInt(values[3]), keyH, this);
                    tile.tileSet = values[0];
                    tile.tileIndex = Integer.parseInt(values[1]);

                    tile.image = tiles.get(tile.tileSet).get(tile.tileIndex).image;
                    tile.collision = tiles.get(tile.tileSet).get(tile.tileIndex).collision;

                    towerLocations.add(tile);
                    mapTileNum[Integer.parseInt(values[2])][Integer.parseInt(values[3])] = tile;
                } else {
                    Tile tile = new Tile();
                    tile.tileSet = values[0];
                    tile.tileIndex = Integer.parseInt(values[1]);

                    tile.image = tiles.get(tile.tileSet).get(tile.tileIndex).image;
                    tile.collision = tiles.get(tile.tileSet).get(tile.tileIndex).collision;

                    mapTileNum[Integer.parseInt(values[2])][Integer.parseInt(values[3])] = tile;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void placeTower (Tower tower, TowerPlacer towerLocation) {
        mapTileNum[towerLocation.location[0]][towerLocation.location[1]] = tower;
        removeTowerQueue.add(towerLocation);
    }

    public void update () {
        for (TowerPlacer towerLocation : removeTowerQueue) {
            towerLocations.remove(towerLocation);
        }
        removeTowerQueue.clear();
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

        //Draw the tiles on screen
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            Tile tile = mapTileNum[worldCol][worldRow];
            if (tile != null) {
                int worldX = worldCol * GamePanel.tileSize;
                int worldY = worldRow * GamePanel.tileSize;
                int screenX = worldX - (int) gp.player.worldX + gp.player.screenX; //screenX is an offset accounting for the centering of the player
                int screenY = worldY - (int) gp.player.worldY + gp.player.screenY; //screenY is an offset accounting for the centering of the player

                //Draw tile if in bounds of screen
                if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - (2 * GamePanel.tileSize) < gp.player.worldX + gp.player.screenX &&
                        worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - (2 * GamePanel.tileSize) < gp.player.worldY + gp.player.screenY) {

                    g2.drawImage(tile.image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
                }
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        //Debug Drawings
        for (TowerPlacer towerLocation : towerLocations) {
            g2.setColor(Color.red);
            g2.drawOval(
                    (int)(towerLocation.solidArea.getCenterX() - towerLocation.solidArea.getRadius() - gp.player.worldX + gp.player.screenX),
                    (int)(towerLocation.solidArea.getCenterY() - towerLocation.solidArea.getRadius() - gp.player.worldY + gp.player.screenY),
                    (int)(towerLocation.solidArea.getRadius() * 2),
                    (int)(towerLocation.solidArea.getRadius() * 2)
            );
        }
    }
}