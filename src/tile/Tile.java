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
    public Tile[] neighbors = new Tile[8];

    /**
     *
     */
    public void setNeighbors (TileManager tileM) {
        for (int i = 0; i < 7; i++) {
            Tile setTile = null;
            switch (i) {
                case 0:

                    setTile = tileM.mapTileNum[tileCol - 1][tileRow - 1];
                    break;
                case 1:
                    setTile = tileM.mapTileNum[tileCol][tileRow - 1];
                    break;
                case 2:
                    setTile = tileM.mapTileNum[tileCol + 1][tileRow - 1];
                    break;
                case 3:
                    setTile = tileM.mapTileNum[tileCol - 1][tileRow];
                    break;
                case 4:
                    setTile = tileM.mapTileNum[tileCol + 1][tileRow];
                    break;
                case 5:
                    setTile = tileM.mapTileNum[tileCol - 1][tileRow + 1];
                    break;
                case 6:
                    setTile = tileM.mapTileNum[tileCol][tileRow + 1];
                    break;
                case 7:
                    setTile = tileM.mapTileNum[tileCol + 1][tileRow + 1];
                    break;
            }
            neighbors[i] = setTile;

        }
    }

    /**
     *
     */
    public void replaceNeighbor (Tile oldNeighbor, Tile newNeighbor) {
        for (int i = 0; i < 7; i++) {
            if (neighbors[i].equals(oldNeighbor)) {
                neighbors[i] = newNeighbor;
                break;
            }
        }
    }

}