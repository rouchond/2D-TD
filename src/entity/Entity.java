package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    /**
     * X,Y Coordinates of the Entity on the tileMap
     */
    public float worldX, worldY;
    /**
     * Entity Sprite (will be updated for anim)
     */
    public BufferedImage img;
    /**
     * Speed that the entity is moving
     */
    public float currentSpeed;
    /**
     * The max speed of the entity
     */
    public float maxSpeed;

    public EntityUtil.Direction direction;
    /**
     * Area that represents the collision box relative to the screen
     */
    public Rectangle solidArea;
    /**
     * Area of collision box relative to the world
     */
    public double solidAreaWorldX, solidAreaWorldY;
    /**
     * State of the collision for the entity
     */
    public boolean collisionOn;

}
