package tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class TileLoader {
    /**
     * The data representation of all the tiles in the game.
     * The keys are the category of tiles (dungeon, ocean, ect.)
     * The values are arrays that store the tiles of the specified category
     */
    HashMap <String, ArrayList<Tile>> Tiles = new HashMap<>();

    /**
     *
     */
    public TileLoader () {
        Tiles.put("dungeon", new ArrayList<>());
        Tiles.get("dungeon").add(new Tile());
        getTileImages();
    }

    /**
     * Get Tile Images
     */
    private void getTileImages () {

        // Load Dungeon Tiles
        File folder = new File("res/tiles/dungeon");

        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path");
        }

        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName)); // Sort files for OS consistency
            int i = 1;
            for (File file : files) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        Tile tile = new Tile();
                        tile.image = img;
                        tile.tileSet = "dungeon";
                        if (i > 1 && i < 16) {
                            tile.collision = true;
                        }
                        tile.tileIndex = i;
                        Tiles.get("dungeon").add(tile);
                        i++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
