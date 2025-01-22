package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
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

    public Moving (GamePanel gp, PhysicsHandler physH, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.physH = physH;
        this.colH = colH;
        this.tileM = tileM;
    }
    @Override
    public void enterState(PlaceholderController controller) {

    }

    @Override
    public void updateState(PlaceholderController controller) {
        move(controller);
        physH.update();
    }

    @Override
    public void exitState(PlaceholderController controller) {

    }

    /**
     *
     */
    private void move (PlaceholderController controller) {
        Vector2 dir = getDirection(controller).normalize();
        System.out.println(dir.x);
        System.out.println(dir.y);
        System.out.println();
        physH.setVelocity(dir);
    }

    /**
     * Finds a valid path to the player and returns a vector in that direction
     * @param controller The controller of this enemy
     */
    private Vector2 getDirection(PlaceholderController controller) {
        // Calculate the vector from enemy to player and the straight-line distance
        float directDx = gp.player.worldX - controller.enemy.worldX;
        float directDy = gp.player.worldY - controller.enemy.worldY;
        float directDistance = (float) Math.sqrt(directDx * directDx + directDy * directDy);

        // Define two distance thresholds creating a small buffer zone
        // The enemy stops when closer than STOP_DISTANCE
        // When stopped, it won't start moving again until the player is further than RESUME_DISTANCE
        float STOP_DISTANCE = GamePanel.tileSize * 0.5f;
        float RESUME_DISTANCE = GamePanel.tileSize * 0.6f;

        // If we're very close to the player, stop moving
        if (directDistance < STOP_DISTANCE) {
            controller.enemy.isMoving = false;
            return new Vector2(0, 0);
        }
        // If we're in the buffer zone and already stopped, maintain the stopped state
        // This prevents rapid switching between moving and stopped states
        else if (directDistance < RESUME_DISTANCE && !controller.enemy.isMoving) {
            return new Vector2(0, 0);
        }

        // We're far enough away, so we should be moving
        controller.enemy.isMoving = true;

        // Get the current tile positions of both enemy and player
        Tile startTile = getEnemyTile(controller);
        Tile targetTile = getPlayerTile();

        // Calculate a path from enemy to player using A* pathfinding
        Pathfinding pathfinder = new Pathfinding();
        ArrayList<Tile> path = pathfinder.findPath(startTile, targetTile);

        // If we found a valid path with at least two tiles (current and next)
        if (path != null && path.size() > 1) {
            // When very close to player (within 1.5 tiles), ignore pathfinding
            // and move directly toward player's position for more natural movement
            if (directDistance < GamePanel.tileSize * 1.5f) {
                return new Vector2(
                        directDx > 0 ? 1 : directDx < 0 ? -1 : 0,  // Move right if player is to right, left if to left
                        directDy > 0 ? 1 : directDy < 0 ? -1 : 0   // Move down if player is below, up if above
                );
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


    private Tile getEnemyTile(PlaceholderController controller) {
        int col = (int) (controller.enemy.worldX / GamePanel.tileSize);
        int row = (int) (controller.enemy.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[col][row];
    }

    private Tile getPlayerTile() {
        int col = (int) (gp.player.worldX / GamePanel.tileSize);
        int row = (int) (gp.player.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[col][row];
    }
}
