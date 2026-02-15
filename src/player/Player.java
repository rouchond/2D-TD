package player;

import Util.Vector2;
import entity.Entity;
import entity.EntityUtil;
import main.*;
import player.states.Idle;
import player.states.Moving;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    public GamePanel gp;
    KeyHandler keyH;
    Camera camera;
    CollisionHandler colH;
    TileManager tileM;
    PhysicsHandler physH;
    PlayerController pController;
    MeleeAttack meleeAttack;

    public Idle idle;
    public Moving moving;

    // Facing direction (normalized vector from player center toward mouse)
    Vector2 facingDir = new Vector2(0, 1); // default facing down

    /**
     * Create the player
     * @param gp GamePanel
     * @param keyH KeyHandler
     */
    public Player(GamePanel gp, KeyHandler keyH, Camera camera, CollisionHandler colH, TileManager tileM){
        this.gp = gp;
        this.keyH = keyH;
        this.camera = camera;
        this.colH = colH;
        this.tileM = tileM;
        this.physH = new PhysicsHandler(this, gp, tileM);
        this.pController = new PlayerController(this);
        this.meleeAttack = new MeleeAttack(this, keyH);

        this.idle = new Idle(this.keyH, physH);
        this.moving = new Moving(this.keyH, this.physH, this.colH);

        solidArea = new Rectangle(8, 16, 32, 32);

        pController.setupState(idle);
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Set initial position and speed of the player
     */
    private void setDefaultValues(){
        worldX = GamePanel.tileSize * 7;
        worldY = GamePanel.tileSize * 10;
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
        updateFacingDirection();
        meleeAttack.update();
        pController.update();
    }

    /**
     * Updates facingDir and direction enum based on mouse position each frame
     */
    private void updateFacingDirection() {
        float playerScreenCenterX = camera.toScreenX(Math.round(worldX + solidArea.x + solidArea.width / 2f));
        float playerScreenCenterY = camera.toScreenY(Math.round(worldY + solidArea.y + solidArea.height / 2f));

        facingDir = computeFacingDirection(playerScreenCenterX, playerScreenCenterY, keyH.mouseX, keyH.mouseY);

        if (facingDir.x != 0 || facingDir.y != 0) {
            // Pass a copy since vectorToDirection mutates its input
            direction = EntityUtil.vectorToDirection(new Vector2(facingDir.x, facingDir.y));
        }
    }

    /**
     * Computes a normalized facing direction from a screen-space player center to a mouse position
     */
    static Vector2 computeFacingDirection(float playerScreenCX, float playerScreenCY, int mouseX, int mouseY) {
        float dx = mouseX - playerScreenCX;
        float dy = mouseY - playerScreenCY;
        Vector2 raw = new Vector2(dx, dy);
        return raw.normalize();
    }

    /**
     * Activates debug drawings if debugMode active
     * @param g2 Graphics Object
     */
    private void debugDrawings(Graphics2D g2) {
        if (gp.debugMode) {
            int screenX = camera.toScreenX((int) worldX + solidArea.x);
            int screenY = camera.toScreenY((int) worldY + solidArea.y);

            // Solid area
            g2.setColor(Color.red);
            g2.drawRect(screenX, screenY, solidArea.width, solidArea.height);

            // Facing direction line (green)
            int playerScreenCX = camera.toScreenX(Math.round(worldX + solidArea.x + solidArea.width / 2f));
            int playerScreenCY = camera.toScreenY(Math.round(worldY + solidArea.y + solidArea.height / 2f));
            g2.setColor(Color.green);
            g2.drawLine(playerScreenCX, playerScreenCY,
                    playerScreenCX + Math.round(facingDir.x * 40),
                    playerScreenCY + Math.round(facingDir.y * 40));

            // Attack hitbox debug
            meleeAttack.drawDebug(g2, camera);
        }
    }

    /**
     * Draw the player with camera offset
     * @param g2 2D graphics object from GamePanel
     */
    public void draw(Graphics2D g2) {
        g2.drawImage(img, camera.toScreenX(Math.round(worldX)), camera.toScreenY(Math.round(worldY)), GamePanel.tileSize, GamePanel.tileSize, null);
        debugDrawings(g2);
    }
}