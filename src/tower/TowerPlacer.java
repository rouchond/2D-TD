package tower;

import Util.Circle;
import main.GamePanel;
import main.KeyHandler;
import player.Player;
import tile.Tile;
import tile.TileManager;

import java.awt.*;

public class TowerPlacer extends Tile {

    /**
     * An invisible area that determines if the player is in range to activate the area
     */
    public Circle solidArea;

    /**
     * Determines whether the player is in range to activate UI to purchase a tower
     */
    private boolean areaActive;

    /**
     * An array holding the column and row of the tower in the tile grid.
     */
    public int[] location;

    KeyHandler keyH;
    TileManager tileM;

    public TowerPlacer (int col, int row, KeyHandler keyH, TileManager tileM) {
        solidArea = new Circle(
                (col * GamePanel.tileSize),
                row * GamePanel.tileSize,
                50
        );

        location = new int[2];
        location[0] = col;
        location[1] = row;

        this.keyH = keyH;
        this.tileM = tileM;
    }

    private void checkActive (Player player) {
        areaActive = false;

        Rectangle playerSolidArea = new Rectangle((int)(player.worldX + player.solidArea.x), (int)(player.worldY + player.solidArea.y), player.solidArea.width, player.solidArea.height);
        if (solidArea.intersects(playerSolidArea)) {
            areaActive = true;
        }
    }

    public void update (Player player) {
        checkActive(player);
        if (areaActive && (keyH.ePressed && !keyH.previousEPressed)) {
            System.out.println("Tower Placed");
            tileM.placeTower(tileM.tempTower, this);
        }
    }
}
