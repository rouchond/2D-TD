package tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

    /**
     * The column this tile is in for the tileGrid
     */
    public int tileCol;

    /**
     * The row this tile is in for the tileGrid
     */
    public int tileRow;

    /**
     * The 8 tiles surrounding the tile
     */


    @Override
    public boolean equals(Object o) {
        if (o == this) {return true;}
        else if (!(o instanceof Tile tile)) {
            return false;
        }
        else {
            return tileCol == tile.tileCol && tileRow == tile.tileRow;
        }
    }
}