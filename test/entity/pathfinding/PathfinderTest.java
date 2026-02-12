package entity.pathfinding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tile.Tile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PathfinderTest {

    private static final int GRID_SIZE = 10;
    private Pathfinder pathfinder;
    private Tile[][] tileMap;

    /**
     * Helper to create a Tile at the given row/col with optional collision.
     */
    private Tile makeTile(int row, int col, boolean collision) {
        Tile t = new Tile();
        t.tileRow = row;
        t.tileCol = col;
        t.collision = collision;
        return t;
    }

    /**
     * Builds a fully walkable tile grid of the given size.
     */
    private Tile[][] makeOpenGrid(int rows, int cols) {
        Tile[][] grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = makeTile(r, c, false);
            }
        }
        return grid;
    }

    @BeforeEach
    void setUp() {
        pathfinder = new Pathfinder(GRID_SIZE, GRID_SIZE);
        tileMap = makeOpenGrid(GRID_SIZE, GRID_SIZE);
        pathfinder.setupNodes(tileMap);
    }

    @Test
    void findPathSameStartAndEnd() {
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[0][0]);

        assertNotNull(path);
        assertEquals(1, path.size());
        assertEquals(0, path.get(0).row);
        assertEquals(0, path.get(0).col);
    }

    @Test
    void findPathAdjacentHorizontal() {
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[0][1]);

        assertNotNull(path);
        // Start and end should be in the path
        assertEquals(0, path.getFirst().col);
        assertEquals(1, path.getLast().col);
    }

    @Test
    void findPathAdjacentVertical() {
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[1][0]);

        assertNotNull(path);
        assertEquals(0, path.getFirst().row);
        assertEquals(1, path.getLast().row);
    }

    @Test
    void findPathStartsAtStartAndEndsAtTarget() {
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[2][3], tileMap[7][8]);

        assertNotNull(path);
        assertEquals(2, path.getFirst().row);
        assertEquals(3, path.getFirst().col);
        assertEquals(7, path.getLast().row);
        assertEquals(8, path.getLast().col);
    }

    @Test
    void findPathUseDiagonalOnOpenGrid() {
        // On an open grid, A* should prefer diagonals
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[3][3]);

        assertNotNull(path);
        // Diagonal path: (0,0) -> (1,1) -> (2,2) -> (3,3) = 4 nodes
        assertEquals(4, path.size());
    }

    @Test
    void findPathAroundWall() {
        // Create a vertical wall at col=3, rows 0-7
        for (int r = 0; r <= 7; r++) {
            tileMap[r][3].collision = true;
        }
        pathfinder.setupNodes(tileMap);

        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[0][5]);

        assertNotNull(path);
        // Path must start at (0,0) and end at (0,5)
        assertEquals(0, path.getFirst().row);
        assertEquals(0, path.getFirst().col);
        assertEquals(0, path.getLast().row);
        assertEquals(5, path.getLast().col);

        // Path should not go through the wall
        for (PathfindingNode node : path) {
            assertFalse(node.col == 3 && node.row <= 7,
                    "Path should not pass through wall at col=3, row=" + node.row);
        }
    }

    @Test
    void findPathReturnsNullWhenBlocked() {
        // Create a complete wall at col=5, blocking all rows
        for (int r = 0; r < GRID_SIZE; r++) {
            tileMap[r][5].collision = true;
        }
        pathfinder.setupNodes(tileMap);

        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[0][9]);

        assertNull(path, "Should return null when no path exists");
    }

    @Test
    void findPathThroughNarrowCorridor() {
        // Block everything except a 1-tile-wide corridor at row=5
        for (int r = 0; r < GRID_SIZE; r++) {
            if (r != 5) {
                tileMap[r][4].collision = true;
                tileMap[r][5].collision = true;
            }
        }
        pathfinder.setupNodes(tileMap);

        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[5][0], tileMap[5][9]);

        assertNotNull(path);
        assertEquals(5, path.getFirst().row);
        assertEquals(0, path.getFirst().col);
        assertEquals(5, path.getLast().row);
        assertEquals(9, path.getLast().col);
    }

    @Test
    void findPathConsecutiveCallsResetState() {
        // First call
        ArrayList<PathfindingNode> path1 = pathfinder.findPath(tileMap[0][0], tileMap[9][9]);
        assertNotNull(path1);

        // Second call should produce a valid path too (internal state is reset)
        ArrayList<PathfindingNode> path2 = pathfinder.findPath(tileMap[9][9], tileMap[0][0]);
        assertNotNull(path2);

        assertEquals(0, path2.getLast().row);
        assertEquals(0, path2.getLast().col);
    }

    @Test
    void findPathIsContinuous() {
        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[0][0], tileMap[5][7]);

        assertNotNull(path);
        // Each step should be adjacent (max 1 tile apart in each axis)
        for (int i = 1; i < path.size(); i++) {
            int dRow = Math.abs(path.get(i).row - path.get(i - 1).row);
            int dCol = Math.abs(path.get(i).col - path.get(i - 1).col);

            assertTrue(dRow <= 1, "Row gap too large at step " + i);
            assertTrue(dCol <= 1, "Col gap too large at step " + i);
            assertFalse(dRow == 0 && dCol == 0, "Path has duplicate node at step " + i);
        }
    }

    @Test
    void findPathDiagonalDoesNotCutCorners() {
        // Place walls to form a corner at (4,4) and (4,5)
        // The path should not cut diagonally through the corner
        tileMap[4][4].collision = true;
        tileMap[4][5].collision = true;
        pathfinder.setupNodes(tileMap);

        ArrayList<PathfindingNode> path = pathfinder.findPath(tileMap[3][4], tileMap[5][4]);

        assertNotNull(path);
        // Path should not contain (4,4) or (4,5)
        for (PathfindingNode node : path) {
            assertFalse(node.row == 4 && node.col == 4);
            assertFalse(node.row == 4 && node.col == 5);
        }
    }

    @Test
    void findPathOnSmallGrid() {
        // Test with a minimal 3x3 grid
        Pathfinder smallPathfinder = new Pathfinder(3, 3);
        Tile[][] smallGrid = makeOpenGrid(3, 3);
        smallPathfinder.setupNodes(smallGrid);

        ArrayList<PathfindingNode> path = smallPathfinder.findPath(smallGrid[0][0], smallGrid[2][2]);

        assertNotNull(path);
        assertEquals(0, path.getFirst().row);
        assertEquals(0, path.getFirst().col);
        assertEquals(2, path.getLast().row);
        assertEquals(2, path.getLast().col);
    }

    @Test
    void findPathReversedIsSameLength() {
        ArrayList<PathfindingNode> forward = pathfinder.findPath(tileMap[1][1], tileMap[8][8]);
        // Reset and find reverse
        ArrayList<PathfindingNode> reverse = pathfinder.findPath(tileMap[8][8], tileMap[1][1]);

        assertNotNull(forward);
        assertNotNull(reverse);
        // On a symmetric open grid, both directions should find equal-length paths
        assertEquals(forward.size(), reverse.size());
    }
}