package entity.pathfinding;

import tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder {

    private final PathfindingNode[][] nodeGrid;

    /**
     * An array of nodes the algorithm is searching
     */
    private ArrayList<PathfindingNode> openList;

    /**
     * An array of nodes the algorithm is finished looking at
     */
    private ArrayList<PathfindingNode> closedList;

    int maxWorldCol;
    int maxWorldRow;

    public Pathfinder (int maxWorldCol, int maxWorldRow) {
        this.maxWorldCol = maxWorldCol;
        this.maxWorldRow = maxWorldRow;
        nodeGrid = new PathfindingNode[maxWorldRow][maxWorldCol];
        openList = new ArrayList<>();
        closedList = new ArrayList<>();
    }

    /**
     * Converts the tilemap into a map of pathfinding nodes with walkable tiles set
     * @param tileMap The tilemap
     */
    public void setupNodes(Tile[][] tileMap) {
        for (Tile[] tileRow : tileMap){
            for (Tile tile : tileRow) {
                if (tile != null) {
                    nodeGrid[tile.tileCol][tile.tileRow] = new PathfindingNode(tile.tileCol, tile.tileRow, tile.collision);
                }
            }
        }
    }


    /**
     * Generates an optimal path of nodes to the target from the entity
     * @param startTile The tile the entity is on
     * @param targetTile The tile the entity is trying to get to
     */
    public ArrayList<PathfindingNode> findPath (Tile startTile, Tile targetTile) {
        resetNodes();

        PathfindingNode target = nodeGrid[targetTile.tileRow][targetTile.tileCol];
        PathfindingNode start = nodeGrid[startTile.tileRow][startTile.tileCol];

        start.gCost = 0;
        start.calculateH(target);
        start.open = true;
        openList.add(start);



        while (!openList.isEmpty()) {
            PathfindingNode current = getLowestFCost();

            if (current.equals(target)) {
                return backtrace(current);
            }

            current.closed = true;
            closedList.add(current);
            getNeighbors(current, target);
        }

        return null;
    }

    /**
     * Gets the neighbors of the node being searched and updates corresponding lists
     * @param current Node being searched
     * @param target Node the entity is finding a path to
     */
    private void getNeighbors (PathfindingNode current, PathfindingNode target) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // Skip the node itself

                // Calculate Neighbors

                int neighborCol = current.col + i;
                int neighborRow = current.row + j;

                float distanceToNeighbor = 1.0f;

                // If a valid diagonal, use diagonal distance, otherwise skip this tile
                if (i != 0 && j != 0) {
                    distanceToNeighbor = 1.41f;

                    if ((nodeGrid[current.row][current.col + i] == null || nodeGrid[current.row + j][current.col] == null )
                            || (!nodeGrid[current.row][current.col + i].walkable
                            || !nodeGrid[current.row + j][current.col].walkable)) {
                        continue;
                    }
                }

                float newG = current.gCost + distanceToNeighbor;

                // If valid neighbor, create node and add to search list
                if (neighborCol >= 0 && neighborCol < maxWorldCol &&
                        neighborRow >= 0 && neighborRow < maxWorldRow) {

                    PathfindingNode neighbor = nodeGrid[neighborRow][neighborCol];

                    if ((neighbor == null || (!neighbor.walkable && neighbor.closed)) ) {
                        continue;
                    }

                    if (!neighbor.open || newG < neighbor.gCost) {
                        neighbor.gCost = newG;
                        neighbor.calculateH(target);
                        neighbor.parent = current;
                    }

                    if (!neighbor.open) {
                        neighbor.open = true;
                        openList.add(neighbor);
                    }
                }


            }

        }

    }

    /**
     * Clear lists and reset node values to default
     */
    private void resetNodes() {
        openList.clear();
        closedList.clear();
        for (PathfindingNode[] nodeRow : nodeGrid) {
            for (PathfindingNode node: nodeRow) {
                if (node != null) {
                    node.open = false;
                    node.gCost = 0;
                    node.hCost = 0;
                    node.parent = null;
                }
            }
        }
    }

    /**
     * Get the lowest F-Cost node in the openList
     */
    private PathfindingNode getLowestFCost() {
        PathfindingNode minNode = openList.getFirst();
        for (PathfindingNode node : openList) {
            if (node.getfCost() < minNode.getfCost()) {
                minNode = node;
            }
        }

        minNode.open = false;
        openList.remove(minNode);
        return minNode;
    }

    /**
     * Recurses through the parent nodes to generate a valid path of nodes
     */
    private ArrayList<PathfindingNode> backtrace (PathfindingNode current) {
        ArrayList<PathfindingNode> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        List<PathfindingNode> newLi = path.reversed();

        ArrayList<PathfindingNode> res = new ArrayList<>(newLi);

        return res;
    }

}
