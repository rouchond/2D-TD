package entity.pathfinding;

import tile.Tile;

public class PathNode {
    private final Tile tile;  // Reference to the actual tile
    public PathNode connection;
    public float G;  // Walking cost from start node
    public float H;  // Heuristic cost to end node

    public PathNode(Tile tile) {
        this.tile = tile;
    }

    public boolean isWalkable() {
        return !tile.collision;  // Use tile's collision property
    }

    public float getF() {
        return G + H;
    }

    // Delegate tile-specific operations to the tile reference
    public Tile[] getNeighbors() {
        return tile.neighbors;
    }

    public int getTileCol() {
        return tile.tileCol;
    }

    public int getTileRow() {
        return tile.tileRow;
    }

    public Tile getTile () {
        return tile;
    }

    public void setConnection (PathNode connection) {
        this.connection = connection;
    }
}