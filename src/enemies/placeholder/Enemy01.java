package enemies.placeholder;

import enemies.placeholder.states.Attack;
import enemies.placeholder.states.Knockback;
import enemies.placeholder.states.Moving;
import entity.Entity;
import entity.EntityUtil;
import entity.pathfinding.Pathfinder;
import main.Camera;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import player.Player;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Enemy01 extends Entity {

    GamePanel gp;

    Camera camera;

    Pathfinder pathfinder;

    TileManager tileM;

    Player player;

    CollisionHandler colH;

    PhysicsHandler physH;

    PlaceholderController eController;

    public Moving moving;

    public Attack attack;

    public Knockback knockback;

    private final int startX;

    private final int startY;

    public Enemy01 (GamePanel gp, Camera camera, Pathfinder pathfinder, CollisionHandler colH, TileManager tileM, int x, int y) {
        this.gp = gp;
        this.camera = camera;
        this.pathfinder = pathfinder;
        this.colH = colH;
        this.tileM = tileM;

        this.startX = x;
        this.startY = y;


        this.player  = this.gp.player;
        this.physH = new PhysicsHandler(this, gp, tileM);

        this.eController = new PlaceholderController(gp, this);
        this.moving = new Moving(this.gp, this.camera, this.pathfinder, this.physH, this.colH, this.tileM);
        this.attack = new Attack(this.gp, this.camera, this.physH, this.colH);
        this.knockback = new Knockback(this.gp, this.camera, this.physH, this.colH, this.tileM);

        solidArea = new Rectangle(8, 16, 32, 32);

        eController.setupState(moving);
        getEnemyImage();
        setDefaultValues();
    }

    private void setDefaultValues() {
        worldX = GamePanel.tileSize * startX;
        worldY = GamePanel.tileSize * startY;
        baseSpeed = 1;
        speed = baseSpeed;
        health = 10;
        direction = EntityUtil.Direction.DOWN;
    }

    private void getEnemyImage () {
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy.png")));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update () {
        tileCollisionOn = false;
        eController.update();
    }

    /**
     * Activates debug drawings if debugMode active
     * @param g2 Graphics Object
     */
    private void debugDrawings(Graphics2D g2) {
        if (gp.debugMode) {
            moving.drawPath(g2);

            float enemyCenterX = worldX + solidArea.x + (solidArea.width / 2f);
            float enemyCenterY = worldY + solidArea.y + (solidArea.height / 2f);

            float targetX = gp.player.worldX + gp.player.solidArea.x + (gp.player.solidArea.width / 2f);
            float targetY = gp.player.worldY + gp.player.solidArea.y + (gp.player.solidArea.height / 2f);

            moving.drawTargetLine(g2, enemyCenterX, enemyCenterY, targetX, targetY);
        }
    }

    public void draw (Graphics2D g2) {
        g2.drawImage(img, camera.toScreenX(Math.round(worldX)), camera.toScreenY(Math.round(worldY)), GamePanel.tileSize, GamePanel.tileSize, null);
        debugDrawings(g2);
    }
}
