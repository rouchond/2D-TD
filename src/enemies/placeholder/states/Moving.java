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
    }

    @Override
    public void exitState(PlaceholderController controller) {

    }

    /**
     *
     */
    private void move (PlaceholderController controller) {
        Vector2 dir = getDirection(controller).normalize();
        physH.setVelocity(dir);
    }

    /**
     * Finds a valid path to the player and returns a vector in that direction
     * @param controller The controller of this enemy
     */
    private Vector2 getDirection (PlaceholderController controller) {
        Tile startTile = getEnemyTile(controller);
        Tile targetTile = getPlayerTile();


        Pathfinding pathfinder = new Pathfinding();
        ArrayList<Tile> path = pathfinder.findPath(startTile, targetTile);
        if (path != null) {
            Tile nextTile = path.getFirst();
            float tileX = nextTile.tileCol * GamePanel.tileSize;
            float tileY = nextTile.tileRow * GamePanel.tileSize;

            int vecX, vecY;

            if (tileX < controller.enemy.worldX) {
                vecX = -1;
            } else if (tileX > controller.enemy.worldX) {
                vecX = 1;
            } else {
                vecX = 0;
            }

            if (tileY < controller.enemy.worldY) {
                vecY = -1;
            } else if (tileY > controller.enemy.worldY) {
                vecY = 1;
            }
            else {
                vecY = 0;
            }

            return new Vector2(vecX, vecY);
        }
        return new Vector2(0,0);
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
