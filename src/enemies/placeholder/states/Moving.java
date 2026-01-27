package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
import entity.EntityUtil;
import entity.pathfinding.Pathfinder;
import entity.pathfinding.PathfindingNode;
import main.*;
import tile.Tile;
import tile.TileManager;

import java.util.ArrayList;

public class Moving implements State<PlaceholderController> {

    GamePanel gp;
    TileManager tileM;
    PhysicsHandler physH;
    CollisionHandler colH;
    Pathfinder pathfinder;

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

    /**
     * The current path the enemy is taking
     */
    private ArrayList<PathfindingNode> currPath = new ArrayList<>();

    /**
     * The tile that enemy is tracking
     */
    private Tile currTarget;

    public Moving (GamePanel gp, Pathfinder pathfinder, PhysicsHandler physH, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.pathfinder = pathfinder;
        this.physH = physH;
        this.colH = colH;
        this.tileM = tileM;
    }
    @Override
    public void enterState(PlaceholderController controller) {
        graceStartTime = System.nanoTime();
        currTarget = getPlayerTile();
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
        // Enemy Movement
        Vector2 moveDir = getDirection(controller);
        System.out.println(moveDir);
    }

    /**
     * Finds a valid path to the player and returns a vector in that direction
     * @param controller The state controller of the entity
     */
    private Vector2 getDirection(PlaceholderController controller) {
        //Update the path if the player moved, or we don't have a path
        if (currPath.isEmpty() || !currTarget.equals(getPlayerTile())) {
            currPath = pathfinder.findPath(getEnemyTile(controller), getPlayerTile());
            currTarget = getPlayerTile();
        }

        //Enemy doesn't move if we don't have a path
        if (currPath == null || currPath.isEmpty()) {
            return new Vector2(0,0);
        }

        PathfindingNode nextNode = currPath.getFirst();

        // Center of next tile
        float targetX = nextNode.col * GamePanel.tileSize + (GamePanel.tileSize / 2f);
        float targetY = nextNode.row * GamePanel.tileSize + (GamePanel.tileSize / 2f);

        // Distance & Direction
        float diffX = targetX - controller.enemy.worldX;
        float diffY = targetY - controller.enemy.worldY;
        float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);

        //If enemy is close to player, stop moving (change to attack state later)
        if (distance < controller.enemy.speed) {
            currPath.removeFirst();
            return new Vector2(0,0);
        }
        else {

            //Needs work
            float dirX = diffX / distance;
            float dirY = diffY / distance;

            return new Vector2(dirX, dirY);

        }

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
