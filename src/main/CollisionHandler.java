package main;

import Util.Vector2;
import entity.Entity;
import entity.EntityUtil;
import tile.Tile;

public class CollisionHandler {
    GamePanel gp;

    /**
     * Creates a collision handler
     * @param gp The Game Panel
     */
    public CollisionHandler (GamePanel gp) {this.gp = gp;}

    /**
     * Checks if entity collision box is overlapping any collidable
     * @param x X position of entity
     * @param y Y position of entity
     * @param entity The entity we're checking collisions
     */
    public boolean isCollidingAt (float x, float y, Entity entity) {
        //Calculate collision edges (-1 for preventing edge snagging)
        float leftEdge   = x + entity.solidArea.x;
        float rightEdge  = x + entity.solidArea.x + entity.solidArea.width - 1;
        float topEdge    = y + entity.solidArea.y;
        float bottomEdge = y + entity.solidArea.y + entity.solidArea.height - 1;

        //Check tiles for the 4 corners
        return (isTileSolid(leftEdge, topEdge) ||
                isTileSolid(rightEdge, topEdge)
                || isTileSolid(leftEdge, bottomEdge)
                || isTileSolid(rightEdge, bottomEdge));
    }

    /**
     * Checks if a specific world coordinate is inside a collidable tile.
     * @param worldX The X coordinate in pixels
     * @param worldY The Y coordinate in pixels
     */
    private boolean isTileSolid (float worldX, float worldY) {
        int col = (int) (worldX / GamePanel.tileSize);
        int row = (int) (worldY / GamePanel.tileSize);

        // If the entity tries to check a tile outside the map, treat it as a wall.
        if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return true;
        }

        return gp.tileM.mapTileNum[row][col].collision;
    }

    /**
     * Checks how far an entity can move before colliding or reaching an empty tile
     * returns a float array that contains the x and y positions the entity will move to
     * @param entity The entity we're checking collisions on
     * @param velocity The velocity the entity is moving at
     */
    public Vector2 checkIncremental(Entity entity, Vector2 velocity) {
        float nextX = entity.worldX + velocity.x;
        float nextY = entity.worldY + velocity.y;

        // Just move if the movement is safe
        if (!isCollidingAt(nextX, nextY, entity)) {
            return new Vector2(nextX, nextY);
        }

        // Incremental Check on long movements
        float currentX = entity.worldX;
        float currentY = entity.worldY;

        // 1. HORIZONTAL STEPPING
        float stepsX = Math.abs(velocity.x);
        float signX = Math.signum(velocity.x);

        for (int i = 0; i < stepsX; i += 2) {
            float checkX = currentX + signX; // Move 2 pixels at a time in the right direction
            if (!isCollidingAt(checkX, currentY, entity)) {
                currentX = checkX;
            } else {
                // We hit a wall! Stop stepping X.
                break;
            }
        }

        // 2. VERTICAL STEPPING (Using the updated currentX!)
        float stepsY = Math.abs(velocity.y);
        float signY = Math.signum(velocity.y);

        for (int i = 0; i < stepsY; i += 2) {
            float checkY = currentY + signY;
            if (!isCollidingAt(currentX, checkY, entity)) {
                currentY = checkY;
            } else {
                // We hit a wall! Stop stepping Y.
                break;
            }
        }

        return new Vector2(currentX, currentY);

    }

}
