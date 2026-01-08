package tile;

import entity.pathfinding.Pathfinder;
import main.Camera;
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

import static java.lang.Math.*;

public class TileManager {
    GamePanel gp;
    KeyHandler keyH;
    Camera camera;
    Pathfinder pathfinder;

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
     * The position of a tile in the tile array [col][row]
     */
    public Tile[][] mapTileNum;

    /**
     * Creates the Tile Grid and loads each tile into the grid
     * @param gp
     */
    public TileManager(GamePanel gp, KeyHandler keyH, Camera camera, Pathfinder pathfinder) {
        this.gp = gp;
        this.keyH = keyH;
        this.camera = camera;
        this.pathfinder = pathfinder;

        mapTileNum = new Tile[gp.maxWorldRow][gp.maxWorldCol];

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
                    tile.tileCol = Integer.parseInt(values[2]);
                    tile.tileRow = Integer.parseInt(values[3]);

                    tile.image = tiles.get(tile.tileSet).get(tile.tileIndex).image;
                    tile.collision = tiles.get(tile.tileSet).get(tile.tileIndex).collision;

                    towerLocations.add(tile);
                    mapTileNum[Integer.parseInt(values[2])][Integer.parseInt(values[3])] = tile;
                } else {
                    Tile tile = new Tile();
                    tile.tileSet = values[0];
                    tile.tileIndex = Integer.parseInt(values[1]);
                    tile.tileCol = Integer.parseInt(values[2]);
                    tile.tileRow = Integer.parseInt(values[3]);

                    tile.image = tiles.get(tile.tileSet).get(tile.tileIndex).image;
                    tile.collision = tiles.get(tile.tileSet).get(tile.tileIndex).collision;

                    mapTileNum[Integer.parseInt(values[2])][Integer.parseInt(values[3])] = tile;
                }
            }

            pathfinder.setupNodes(mapTileNum);

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

        //Calculate Bounds for tiles in screen
        int startCol = max(0,(int) floor(camera.getX() / GamePanel.tileSize));
        int startRow = max(0, (int) floor(camera.getY() / GamePanel.tileSize));

        int endCol = min(mapTileNum[0].length, (int) ceil((camera.getX() + camera.screenWidth) / GamePanel.tileSize));
        int endRow = min(mapTileNum.length, (int) ceil((camera.getY() + camera.screenHeight) / GamePanel.tileSize));

        //Draw every tile in screen
        for (int row = startRow; row <= endRow; row++){
            for (int col = startCol; col <= endCol; col++) {
                Tile tile = mapTileNum[col][row];
                if (tile != null) {
                    int worldX = col * GamePanel.tileSize;
                    int worldY = row * GamePanel.tileSize;

                    g2.drawImage(tile.image, camera.toScreenX(worldX), camera.toScreenY(worldY), GamePanel.tileSize, GamePanel.tileSize, null);
                }
            }
        }

        //Debug Drawings
//        for (TowerPlacer towerLocation : towerLocations) {
//            g2.setColor(Color.red);
//            g2.drawOval(
//                    (int)(towerLocation.solidArea.getCenterX() - towerLocation.solidArea.getRadius() - gp.player.worldX + gp.player.screenX),
//                    (int)(towerLocation.solidArea.getCenterY() - towerLocation.solidArea.getRadius() - gp.player.worldY + gp.player.screenY),
//                    (int)(towerLocation.solidArea.getRadius() * 2),
//                    (int)(towerLocation.solidArea.getRadius() * 2)
//            );
//        }
    }
}