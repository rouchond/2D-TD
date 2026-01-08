package enemies.placeholder;

import enemies.placeholder.states.Attack;
import enemies.placeholder.states.Knockback;
import enemies.placeholder.states.Moving;
import entity.Entity;
import entity.EntityUtil;
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

    public Enemy01 (GamePanel gp, Camera camera, CollisionHandler colH, TileManager tileM, int x, int y) {
        this.gp = gp;
        this.camera = camera;
        this.colH = colH;
        this.tileM = tileM;

        this.startX = x;
        this.startY = y;


        this.player  = this.gp.player;
        this.physH = new PhysicsHandler(this, gp, tileM);

        this.eController = new PlaceholderController(gp, this);
        this.moving = new Moving(this.gp,this.physH, this.colH, this.tileM);
        this.attack = new Attack(this.gp, this.physH, this.colH);
        this.knockback = new Knockback(this.gp, this.physH, this.colH, this.tileM);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaWorldX = solidArea.x;
        solidAreaWorldY = solidArea.y;

        eController.setupState(moving);
        getEnemyImage();
        setDefaultValues();
    }

    private void setDefaultValues() {
        worldX = GamePanel.tileSize * startX;
        worldY = GamePanel.tileSize * startY;
        baseSpeed = 5;
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

    public void draw (Graphics2D g2) {
        g2.drawImage(img, camera.toScreenX(Math.round(worldX)), camera.toScreenY(Math.round(worldY)), GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
