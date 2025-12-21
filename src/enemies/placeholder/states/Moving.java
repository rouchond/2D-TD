package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
import entity.EntityUtil;
import entity.pathfinding.Pathfinding;
import main.*;
import tile.Tile;
import tile.TileManager;

import java.util.ArrayList;

public class Moving implements State<PlaceholderController> {

    GamePanel gp;
    TileManager tileM;
    PhysicsHandler physH;
    CollisionHandler colH;

    /**
     * A flag that determines if the enemy should transition to the attack state
     */
    private boolean canAttack;

    /**
     * A grace period when switching to this state that must be elapsed before
     * enemy can attack
     */
    private final float attackGrace = 1.5f;

    /**
     * Tracks the start of the grace period
     */
    private long graceStartTime;

    public Moving (GamePanel gp, PhysicsHandler physH, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.physH = physH;
        this.colH = colH;
        this.tileM = tileM;
    }
    @Override
    public void enterState(PlaceholderController controller) {
        graceStartTime = System.nanoTime();
    }

    /**
     * Uses A* pathfinding to determine the direction to move the entity. If the entity
     * is in range for attacking, switch to the attack state.
     */
    @Override
    public void updateState(PlaceholderController controller) {
        move(controller);
        if (canAttack) {
            controller.changeState(controller.enemy.attack);
        }
        else {
            physH.update();
        }
    }

    /**
     * Resets the can attack state when exiting the state
     * @param controller The state controller of the entity.
     */
    @Override
    public void exitState(PlaceholderController controller) {
        canAttack = false;
    }

    /**
     * Generates a path and moves the player in that direction
     * @param controller The state controller of the enemy
     */
    private void move (PlaceholderController controller) {
        if (!getDirection(controller).equals(new Vector2(0,0))) {
            controller.enemy.direction = EntityUtil.vectorToDirection(getDirection(controller));
        }
        Vector2 dir = getDirection(controller).normalize();
        if (!canAttack) {
            physH.setVelocity(dir);
        }
    }

    /**
     * Finds a valid path to the player and returns a vector in that direction
     * @param controller The state controller of the entity
     */
    private Vector2 getDirection(PlaceholderController controller) {
        // Calculate the vector from enemy to player and the straight-line distance
        float directDx = gp.player.worldX - controller.enemy.worldX;
        float directDy = gp.player.worldY - controller.enemy.worldY;
        float directDistance = (float) Math.sqrt(directDx * directDx + directDy * directDy);

        // Get the current tile positions of both enemy and player
        Tile startTile = getEnemyTile(controller);
        Tile targetTile = getPlayerTile();

        // Calculate a path from enemy to player using A* pathfinding
        Pathfinding pathfinder = new Pathfinding();
        ArrayList<Tile> path = pathfinder.findPath(startTile, targetTile);

        // If we found a valid path with at least two tiles (current and next)
        if (path != null && path.size() > 1) {
            // When very close to player (within 1.5 tiles), activate flag to enter attack state
            if (directDistance < GamePanel.tileSize * 1.5f) {
                float graceDuration = (System.nanoTime() - graceStartTime) / 1_000_000_000f;
                // Check if the grace period for attacking has been met
                if (graceDuration > attackGrace) {
                    canAttack = true;
                }
                return new Vector2(0,0);
            }
            // When farther away, follow the calculated path
            else {
                Tile nextTile = path.get(1);  // Get the next tile in our path
                // Convert tile coordinates to world coordinates, targeting tile center
                float targetX = nextTile.tileCol * GamePanel.tileSize + (GamePanel.tileSize / 2f);
                float targetY = nextTile.tileRow * GamePanel.tileSize + (GamePanel.tileSize / 2f);

                // Calculate direction to next tile
                float pathDx = targetX - controller.enemy.worldX;
                float pathDy = targetY - controller.enemy.worldY;

                // Return a normalized direction vector (-1, 0, or 1 for each component)
                return new Vector2(
                        pathDx > 0 ? 1 : pathDx < 0 ? -1 : 0,
                        pathDy > 0 ? 1 : pathDy < 0 ? -1 : 0
                );
            }
        }

        // If we couldn't find a path or are already at the target, don't move
        return new Vector2(0, 0);
    }

    /**
     * Gets the tile that the enemy is on
     * @param controller The state controller of the entity
     */
    private Tile getEnemyTile(PlaceholderController controller) {
        int col = (int) (controller.enemy.worldX / GamePanel.tileSize);
        int row = (int) (controller.enemy.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[col][row];
    }

    /**
     * Gets the tile that the player is on
     */
    private Tile getPlayerTile() {
        int col = (int) (gp.player.worldX / GamePanel.tileSize);
        int row = (int) (gp.player.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[col][row];
    }
}
