package enemies;

import enemies.placeholder.Enemy01;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;

public class EnemyHandler {

    GamePanel gp;

    CollisionHandler colH;


    ArrayList<Enemy01> enemies = new ArrayList<>();

    public EnemyHandler (GamePanel gp, CollisionHandler colH) {
        this.gp = gp;
        this.colH = colH;
        enemies.add(new Enemy01(gp, colH));
    }

    public void update () {

    }

    public void draw (Graphics2D g2) {
        // Draw each enemy
        for (Enemy01 enemy : enemies) {
            enemy.draw(g2);
        }
    }

}
