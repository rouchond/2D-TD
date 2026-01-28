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

    /**
     * A counter for when to calculate a path again
     */
    private int frameCounter = 0;

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

        if (canAttack) {
            System.out.println("ready to attack");
            physH.setVelocity(new Vector2(0,0));
            //controller.changeState(controller.enemy.attack);
        }
        else {
            move(controller);
        }
        physH.update();
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
        physH.setVelocity(getDirection(controller).scale(controller.enemy.speed));
        //System.out.println("Velocity: " + physH.getVelocity().x + ", " + physH.getVelocity().y);
    }

    /**
     * Finds a valid path to the player and returns a vector in that direction
     * @param controller The state controller of the entity
     */
    private Vector2 getDirection(PlaceholderController controller) {
        frameCounter++;

        // Enemy Center
        float enemyX = controller.enemy.worldX + controller.enemy.solidArea.x + (controller.enemy.solidArea.width / 2f);
        float enemyY = controller.enemy.worldY + controller.enemy.solidArea.y + (controller.enemy.solidArea.height / 2f);

        // Player Center
        float playerX = gp.player.worldX + gp.player.solidArea.x + (gp.player.solidArea.width / 2f);
        float playerY = gp.player.worldY + gp.player.solidArea.y + (gp.player.solidArea.height / 2f);

        // Distance & Direction of player tile
        float pDiffX = playerX - enemyX;
        float pDiffY = playerY - enemyY;

        Vector2 pDir = new Vector2(pDiffX, pDiffY);
        float pDistance = pDir.length();

        int distToTarget = Math.abs(currTarget.tileCol - getPlayerTile().tileCol) +
                Math.abs(currTarget.tileRow - getPlayerTile().tileRow);

        //Update the path if the player moved out of the current tile, or we don't have a path
        if (currPath.isEmpty() || distToTarget > 1 || frameCounter >= 20) {
            currPath = pathfinder.findPath(getEnemyTile(controller), getPlayerTile());
            currTarget = getPlayerTile();
            frameCounter = 0;

            //Check if we should skip the first node if we're practically on it
            if (currPath.size() > 1) {
                float distToFirst = getDistanceToNode(controller, currPath.get(0));
                float distToSecond = getDistanceToNode(controller, currPath.get(1));

                // If the second node is actually more convenient, skip the first
                if (distToFirst < GamePanel.tileSize * 0.8f || distToSecond < distToFirst) {
                    currPath.removeFirst();
                }
            }
        }

        //If enemy is close to player, change to direct follow
        float attackRange = GamePanel.tileSize * 1.5f;
        float directTargetDist = GamePanel.tileSize * 4.0f;
        if (pDistance < directTargetDist) {

            // Stop if in range
            if (pDistance <= attackRange) {
                this.canAttack = true;
                return new Vector2(0, 0);
            }

            // Walk straight to player
            this.canAttack = false;
            return pDir.normalize();
        }

        // A* logic if enemy is far away

        //Enemy doesn't move if we don't have a path
        if (currPath == null || currPath.isEmpty()) {
            return new Vector2(0,0);
        }

        PathfindingNode nextNode = currPath.getFirst();

        // Center of next tile in path
        float targetX = (nextNode.col * GamePanel.tileSize) + (GamePanel.tileSize / 2f);
        float targetY = (nextNode.row * GamePanel.tileSize) + (GamePanel.tileSize / 2f);

        // Distance & Direction of next tile
        float diffX = targetX - enemyX;
        float diffY = targetY - enemyY;

        Vector2 dir = new Vector2(diffX, diffY);
        float distance = dir.length();

        // Follow path if enemy is further away
        if (distance < controller.enemy.speed) {
            currPath.removeFirst();
            if (currPath.isEmpty()) {
                return new Vector2(0,0);
            }
            else {
                //Recalculate target for next tile
                nextNode = currPath.getFirst();
                targetX = (nextNode.col * GamePanel.tileSize) + (GamePanel.tileSize / 2f);
                targetY = (nextNode.row * GamePanel.tileSize) + (GamePanel.tileSize / 2f);
                dir = new Vector2(targetX - enemyX, targetY - enemyY);
            }
        }

        this.canAttack = false;
        return dir.normalize();

    }

    /**
     * Returns the distance between the enemy and the given node
     * @param controller The state controller of the entity
     * @param node The node the method is calculating distance for
     */
    private float getDistanceToNode(PlaceholderController controller, PathfindingNode node) {
        // Center of enemy entity
        float enemyCenterX = controller.enemy.worldX + controller.enemy.solidArea.x + (controller.enemy.solidArea.width / 2f);
        float enemyCenterY = controller.enemy.worldY + controller.enemy.solidArea.y + (controller.enemy.solidArea.height / 2f);

        // Center of node tile
        float nodeCenterX = (node.col * GamePanel.tileSize) + (GamePanel.tileSize / 2f);
        float nodeCenterY = (node.row * GamePanel.tileSize) + (GamePanel.tileSize / 2f);

        // Calculate distance
        Vector2 vec = new Vector2(nodeCenterX - enemyCenterX, nodeCenterY - enemyCenterY);
        return vec.length();
    }

    /**
     * Gets the tile that the enemy is on
     * @param controller The state controller of the entity
     */
    private Tile getEnemyTile(PlaceholderController controller) {
        int col = (int) (controller.enemy.worldX / GamePanel.tileSize);
        int row = (int) (controller.enemy.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[row][col];
    }

    /**
     * Gets the tile that the player is on
     */
    private Tile getPlayerTile() {
        int col = (int) (gp.player.worldX / GamePanel.tileSize);
        int row = (int) (gp.player.worldY / GamePanel.tileSize);
        return tileM.mapTileNum[row][col];
    }
}
