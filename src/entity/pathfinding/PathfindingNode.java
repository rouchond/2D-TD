package entity.pathfinding;

public class PathfindingNode {

    /**
     * Distance from starting tile
     */
    public float gCost;

    /**
     * Estimated distance to target tile
     */
    public float hCost;

    /**
     * Can the entity walk on this tile
     */
    public boolean walkable;

    /**
     * The node that the entity comes from to get to this tile
     */
    public PathfindingNode parent;

    /**
     * Has the node been traversed
     */
    public boolean open = false;

    /**
     * Has the node been processed
     */
    public boolean closed = false;

    int col, row;

    public PathfindingNode (int col, int row, boolean walkable) {
        this.col = col;
        this.row = row;
        this.walkable = walkable;
    }


    public float getfCost () {return hCost + gCost;}
    public boolean equals (PathfindingNode other) {return this.col == other.col && this.row == other.row;}

    /**
     * Calculates the estimated distance the node has to the target
     * @param target The node the entity is trying to get to
     */
    public void calculateH (PathfindingNode target) {
        double xDist = Math.abs(this.col - target.col);
        double yDist = Math.abs(this.row - target.row);

        this.hCost = (float) Math.sqrt(xDist * xDist + yDist * yDist);
    }

    @Override
    public String toString() {
        return "Node[" + col + "," + row + "] G:" + gCost + " H:" + hCost;
    }
}
