package enemies.placeholder;

import enemies.placeholder.states.Moving;
import entity.Entity;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import main.State;
import player.Player;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Enemy01 extends Entity {

    GamePanel gp;

    TileManager tileM;

    Player player;

    CollisionHandler colH;

    PhysicsHandler physH;

    PlaceholderController eController;

    public Moving moving;

    public boolean isMoving;

    public Enemy01 (GamePanel gp, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.colH = colH;
        this.tileM = tileM;

        this.player  = this.gp.player;
        this.physH = new PhysicsHandler(this, gp);

        this.eController = new PlaceholderController(gp, this);
        this.moving = new Moving(this.gp,this.physH, this.colH, this.tileM);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaWorldX = solidArea.x;
        solidAreaWorldY = solidArea.y;

        eController.setupState(moving);
        getEnemyImage();
        setDefaultValues();
    }

    private void setDefaultValues() {
        worldX = GamePanel.tileSize * 11;
        worldY = GamePanel.tileSize * 4;
        speed = 5;
        health = 10;
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
        collisionOn = false;
        eController.update();
    }

    public void draw (Graphics2D g2) {
        int screenX = (int) (worldX -  player.worldX + player.screenX); //screenX is an offset accounting for the centering of the player
        int screenY = (int) (worldY - player.worldY + player.screenY); //screenY is an offset accounting for the centering of the player

        g2.drawImage(img, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
