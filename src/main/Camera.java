package main;

import entity.Entity;

import static java.lang.Math.round;

public class Camera {

    /**
     * The X position in the world
     */
    private float x;

    /**
     * The Y position in the world
     */
    private float y;

    /**
     * The ideal X position (for lerping)
     */
    public float targetX;

    /**
     * The ideal Y position (for lerping)
     */
    public float targetY;

    /**
     * The weight of the camera
     */
    private float lerpSpeed = 0.1f;

    public int screenWidth, screenHeight;

    /**
     * The camera of the game
     * @param screenWidth width of the screen
     * @param screenHeight height of the screen
     */
    public Camera (int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Entity target) {
        // Calculate where the camera should go based on the position of the target
        // then center it to the screen
        targetX = target.worldX - (float) (screenWidth / 2);
        targetY = target.worldY - (float) (screenHeight / 2);

        this.x += (targetX - this.x) * lerpSpeed;
        this.y += (targetY - this.y) * lerpSpeed;
    }


    public int toScreenX(int worldX){return round(worldX - this.x);}
    public int toScreenY(int worldY){return round(worldY - this.y);}

    public float getX () {return this.x;}
    public float getY () {return this.y;}
}



