package player;

import Util.Vector2;
import entity.Entity;
import entity.EntityUtil;
import main.CollisionHandler;
import main.GamePanel;
import main.KeyHandler;
import main.PhysicsHandler;
import player.states.Idle;
import player.states.Moving;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    /**
     * X pos of the screen
     */
    public final int screenX;

    /**
     * Y pos of the screen
     */
    public final int screenY;


    public GamePanel gp;
    KeyHandler keyH;
    CollisionHandler colH;
    PhysicsHandler physH;
    PlayerController pController;

    public Idle idle;
    public Moving moving;

    /**
     * Create the player
     * @param gp GamePanel
     * @param keyH KeyHandler
     */
    public Player(GamePanel gp, KeyHandler keyH, CollisionHandler colH){
        this.gp = gp;
        this.keyH = keyH;
        this.colH = colH;
        this.physH = new PhysicsHandler(this, gp);
        this.pController = new PlayerController(this, this.keyH);

        this.idle = new Idle(this.keyH, physH);
        this.moving = new Moving(this.keyH, this.physH);

        screenX = gp.screenWidth/2 - (GamePanel.tileSize);
        screenY = gp.screenHeight/2 - (GamePanel.tileSize);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaWorldX = solidArea.x;
        solidAreaWorldY = solidArea.y;

        pController.setupState(idle);
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Set initial position and speed of the player
     */
    private void setDefaultValues(){
        worldX = GamePanel.tileSize * 7;
        worldY = GamePanel.tileSize * 5;
        speed = 5;
        direction = EntityUtil.Direction.DOWN;
    }

    /**
     * Load player sprite
     */
    private void getPlayerImage() {
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Ronin.png")));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Logic that is updated every frame
     */
    public void update() {
        collisionOn = false;
        colH.checkTile(this);
        pController.update();
    }

    /**
     * Draw the player with camera offset
     * @param g2 2D graphics object from GamePanel
     */
    public void draw(Graphics2D g2) {
        g2.drawImage(img, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);

        //Debug Drawings
        g2.setColor(Color.red);
        g2.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
    }
}
