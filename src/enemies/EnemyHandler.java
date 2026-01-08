package enemies;

import enemies.placeholder.Enemy01;
import main.Camera;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;

public class EnemyHandler {

    GamePanel gp;

    Camera camera;

    TileManager tileM;

    CollisionHandler colH;

    ArrayList<Enemy01> enemies = new ArrayList<>();

    public EnemyHandler (GamePanel gp, Camera camera, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.camera = camera;
        this.colH = colH;
        this.tileM = tileM;
        enemies.add(new Enemy01(gp, camera, colH, tileM, 11, 4));
        //enemies.add(new Enemy01(gp, colH, tileM, 8, 6));
    }

    public void update () {
        for (Enemy01 enemy : enemies) {
            enemy.update();
        }
    }

    public void draw (Graphics2D g2) {
        // Draw each enemy
        for (Enemy01 enemy : enemies) {
            enemy.draw(g2);
        }
    }

}
