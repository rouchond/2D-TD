package entity.pathfinding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathfindingNodeTest {

    @Test
    void constructorSetsFields() {
        PathfindingNode node = new PathfindingNode(3, 5, true);

        assertEquals(3, node.row);
        assertEquals(5, node.col);
        assertTrue(node.walkable);
    }

    @Test
    void constructorDefaultValues() {
        PathfindingNode node = new PathfindingNode(0, 0, false);

        assertFalse(node.walkable);
        assertFalse(node.open);
        assertFalse(node.closed);
        assertEquals(0f, node.gCost);
        assertEquals(0f, node.hCost);
        assertNull(node.parent);
    }

    @Test
    void getFCostReturnsSumOfGAndH() {
        PathfindingNode node = new PathfindingNode(0, 0, true);
        node.gCost = 3.0f;
        node.hCost = 4.0f;

        assertEquals(7.0f, node.getfCost(), 0.001f);
    }

    @Test
    void equalsSamePosition() {
        PathfindingNode a = new PathfindingNode(2, 3, true);
        PathfindingNode b = new PathfindingNode(2, 3, false);

        assertTrue(a.equals(b));
    }

    @Test
    void equalsDifferentPosition() {
        PathfindingNode a = new PathfindingNode(2, 3, true);
        PathfindingNode b = new PathfindingNode(4, 5, true);

        assertFalse(a.equals(b));
    }

    @Test
    void equalsSameRowDifferentCol() {
        PathfindingNode a = new PathfindingNode(2, 3, true);
        PathfindingNode b = new PathfindingNode(2, 5, true);

        assertFalse(a.equals(b));
    }

    @Test
    void equalsSameColDifferentRow() {
        PathfindingNode a = new PathfindingNode(2, 3, true);
        PathfindingNode b = new PathfindingNode(4, 3, true);

        assertFalse(a.equals(b));
    }

    @Test
    void calculateHAdjacentHorizontal() {
        PathfindingNode node = new PathfindingNode(0, 0, true);
        PathfindingNode target = new PathfindingNode(0, 1, true);

        node.calculateH(target);

        assertEquals(1.0f, node.hCost, 0.001f);
    }

    @Test
    void calculateHAdjacentVertical() {
        PathfindingNode node = new PathfindingNode(0, 0, true);
        PathfindingNode target = new PathfindingNode(1, 0, true);

        node.calculateH(target);

        assertEquals(1.0f, node.hCost, 0.001f);
    }

    @Test
    void calculateHDiagonal() {
        PathfindingNode node = new PathfindingNode(0, 0, true);
        PathfindingNode target = new PathfindingNode(1, 1, true);

        node.calculateH(target);

        // sqrt(1^2 + 1^2) = sqrt(2) ≈ 1.414
        assertEquals(Math.sqrt(2), node.hCost, 0.01f);
    }

    @Test
    void calculateHLargerDistance() {
        PathfindingNode node = new PathfindingNode(0, 0, true);
        PathfindingNode target = new PathfindingNode(3, 4, true);

        node.calculateH(target);

        // sqrt(4^2 + 3^2) = sqrt(25) = 5
        assertEquals(5.0f, node.hCost, 0.001f);
    }

    @Test
    void calculateHSameNode() {
        PathfindingNode node = new PathfindingNode(3, 3, true);
        PathfindingNode target = new PathfindingNode(3, 3, true);

        node.calculateH(target);

        assertEquals(0f, node.hCost, 0.001f);
    }

    @Test
    void toStringFormat() {
        PathfindingNode node = new PathfindingNode(2, 5, true);
        node.gCost = 1.0f;
        node.hCost = 3.0f;

        String str = node.toString();

        assertTrue(str.contains("5"));   // col
        assertTrue(str.contains("2"));   // row
        assertTrue(str.contains("1.0")); // gCost
        assertTrue(str.contains("3.0")); // hCost
    }
}