package tower;

import Util.Circle;
import main.GamePanel;
import main.KeyHandler;
import player.Player;
import tile.Tile;

import java.awt.*;

public class TowerPlacer extends Tile {

    public Circle solidArea;
    private boolean areaActive;
    KeyHandler keyH;

    public TowerPlacer (int col, int row, KeyHandler keyH) {
        solidArea = new Circle(
                (col * GamePanel.tileSize),
                row * GamePanel.tileSize,
                50
        );

        this.keyH = keyH;
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
        //System.out.println((keyH.ePressed && !keyH.previousEPressed));
        if (areaActive && (keyH.ePressed && !keyH.previousEPressed)) {
            System.out.println("Tower Placed");
        }
    }
}
