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
    public float speed;
    /**
     * The speed the entity moves without any addons.
     */
    public float baseSpeed;
    public EntityUtil.Direction direction;
    /**
     * Area that represents the collision box (centered on the entity)
     */
    public Rectangle solidArea;

    /**
     * State of the tile collision for the entity
     */
    public boolean tileCollisionOn;

    /**
     * State of the entity collision for the entity
     */
    public boolean entityCollisionOn;

    /**
     * Determines whether an entity is hostile to the player
     */
    public boolean hostile;

    /**
     * Health of the entity
     */
    public int health;

}
