package entity.pathfinding;

import tile.Tile;
import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinding {
    // This map helps us keep track of which PathNode corresponds to which Tile
    // during the pathfinding process
    private HashMap<Tile, PathNode> nodeMap;

    public ArrayList<Tile> findPath(Tile startTile, Tile targetTile) {
        // Initialize our node mapping for this pathfinding operation
        nodeMap = new HashMap<>();

        // Create PathNodes for our start and target tiles
        PathNode startNode = createPathNode(startTile);
        PathNode targetNode = createPathNode(targetTile);

        // If either start or target is null/unwalkable, no path is possible
        if (!isWalkable(startTile) || !isWalkable(targetTile)) {
            return null;
        }

        ArrayList<PathNode> openList = new ArrayList<>();
        ArrayList<PathNode> closedList = new ArrayList<>();

        openList.add(startNode);

        while (!openList.isEmpty()) {
            // Find the node with the lowest F cost
            PathNode currentNode = openList.getFirst();
            for (PathNode node : openList) {
                if (node.getF() < currentNode.getF() ||
                        (node.getF() == currentNode.getF() && node.H < currentNode.H)) {
                    currentNode = node;
                }
            }

            closedList.add(currentNode);
            openList.remove(currentNode);

            // Check if we've reached our target
            if (currentNode.getTile().equals(targetTile)) {
                return reconstructPath(currentNode, startNode);
            }

            // Process all neighboring tiles
            for (Tile neighborTile : currentNode.getTile().neighbors) {
                // Skip if null or not walkable
                if (neighborTile == null || !isWalkable(neighborTile)) {
                    continue;
                }

                // Get or create PathNode for this neighbor
                PathNode neighborNode = getOrCreatePathNode(neighborTile);

                // Skip if this node is already in the closed list
                if (closedList.contains(neighborNode)) {
                    continue;
                }

                // Calculate new path cost to this neighbor
                float newG = currentNode.G + calculateDistance(currentNode, neighborNode);

                // If this is a better path or node hasn't been processed yet
                if (newG < neighborNode.G || !openList.contains(neighborNode)) {
                    neighborNode.G = newG;
                    neighborNode.H = calculateHeuristic(neighborNode, targetNode);
                    neighborNode.setConnection(currentNode);

                    if (!openList.contains(neighborNode)) {
                        openList.add(neighborNode);
                    }
                }
            }
        }

        // No path found
        return null;
    }

    private PathNode createPathNode(Tile tile) {
        PathNode node = new PathNode(tile);
        nodeMap.put(tile, node);
        return node;
    }

    private PathNode getOrCreatePathNode(Tile tile) {
        return nodeMap.computeIfAbsent(tile, PathNode::new);
    }

    private boolean isWalkable(Tile tile) {
        return !tile.collision;
    }

    private float calculateDistance(PathNode nodeA, PathNode nodeB) {
        // Calculate actual distance between nodes
        // For diagonal movement, you might want to use 1.414 (sqrt(2)) instead of 1
        int deltaCol = Math.abs(nodeA.getTileCol() - nodeB.getTileCol());
        int deltaRow = Math.abs(nodeA.getTileRow() - nodeB.getTileRow());

        if (deltaCol == 1 && deltaRow == 1) {
            return 1.414f; // Diagonal movement
        }
        return 1.0f; // Horizontal or vertical movement
    }

    private float calculateHeuristic(PathNode current, PathNode target) {
        // Manhattan distance heuristic
        return Math.abs(current.getTileCol() - target.getTileCol()) +
                Math.abs(current.getTileRow() - target.getTileRow());
    }

    private ArrayList<Tile> reconstructPath(PathNode endNode, PathNode startNode) {
        ArrayList<Tile> path = new ArrayList<>();
        PathNode currentNode = endNode;

        while (currentNode != startNode) {
            path.addFirst(currentNode.getTile()); // Add to front of list to maintain correct order
            currentNode = currentNode.connection;
        }
        path.addFirst(startNode.getTile());

        return path;
    }
}