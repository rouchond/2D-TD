package player;

import entity.Entity;
import entity.EntityUtil;
import main.CollisionHandler;
import main.GamePanel;
import main.KeyHandler;

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

    /**
     * Create the player
     * @param gp GamePanel
     * @param keyH KeyHandler
     */
    public Player(GamePanel gp, KeyHandler keyH, CollisionHandler colH){
        this.gp = gp;
        this.keyH = keyH;
        this.colH = colH;

        screenX = gp.screenWidth/2 - (GamePanel.tileSize);
        screenY = gp.screenHeight/2 - (GamePanel.tileSize);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaWorldX = solidArea.x;
        solidAreaWorldY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Set initial position and speed of the player
     */
    private void setDefaultValues(){
        worldX = GamePanel.tileSize * 7;
        worldY = GamePanel.tileSize * 5;
        maxSpeed = 5;
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
        if (keyH.keyDown && !keyH.ePressed) {
            if (keyH.downPressed) {
                direction = EntityUtil.Direction.DOWN;
            } else if (keyH.upPressed) {
                direction = EntityUtil.Direction.UP;
            } else if (keyH.rightPressed) {
                direction = EntityUtil.Direction.RIGHT;
            } else if (keyH.leftPressed) {
                direction = EntityUtil.Direction.LEFT;
            }

            collisionOn = false;
            colH.checkTile(this);

            if (!collisionOn) {
                switch (direction) {
                    case UP:
                        worldY -= maxSpeed;
                        break;
                    case DOWN:
                        worldY += maxSpeed;
                        break;
                    case LEFT:
                        worldX -= maxSpeed;
                        break;
                    case RIGHT:
                        worldX += maxSpeed;
                        break;
                }
            }
        }
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
