package player;

import entity.Entity;
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

    /**
     * Create the player
     * @param gp GamePanel
     * @param keyH KeyHandler
     */
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize);
        screenY = gp.screenHeight/2 - (gp.tileSize);

        solidArea = new Rectangle();

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Set initial position and speed of the player
     */
    private void setDefaultValues(){
        worldX = gp.tileSize * 5;
        worldY = 0;
        maxSpeed = 5;
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
        if (keyH.downPressed) {
            worldY += maxSpeed;
        } else if (keyH.upPressed) {
            worldY -= maxSpeed;
        } else if (keyH.rightPressed) {
            worldX += maxSpeed;
        } else if (keyH.leftPressed) {
            worldX -= maxSpeed;
        }
    }

    /**
     * Draw the player with camera offset
     * @param g2 2D graphics object from GamePanel
     */
    public void draw(Graphics2D g2) {
        g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
