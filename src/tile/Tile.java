package tile;

import java.awt.image.BufferedImage;

public class Tile {

    /**
     * The tileSet the tile belongs to
     */
    public String tileSet;

    /**
     * The index of the tile in the tileLoader array
     */
    public int tileIndex;

    /**
     * Image of the tile
     */
    public BufferedImage image;

    /**
     * A property on whether a tile has collisions or not
     */
    public boolean collision = false;


}