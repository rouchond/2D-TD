package main;

import Util.Vector2;
import entity.Entity;
import tile.Tile;
import tile.TileManager;

public class PhysicsHandler {

    /**
     * The current velocity of the entity
     */
    public Vector2 velocity;

    /**
     * The velocity of the entity without forces applied
     */
    private final Vector2 defaultVelocity = new Vector2(0,0);

    public boolean dash;

    Entity entity;
    GamePanel gp;
    TileManager tileM;

    public PhysicsHandler (Entity entity, GamePanel gp, TileManager tileM) {
        this.entity = entity;
        this.velocity = defaultVelocity;
        this.gp = gp;
        this.tileM = tileM;
    }

    /**
     * Sets the velocity of the entity
     * @param newVelocity The direction that the entity is moving
     */
    public void setVelocity (Vector2 newVelocity) {
        velocity.x = (newVelocity.x * entity.speed * gp.deltaTime);
        velocity.y = (newVelocity.y * entity.speed * gp.deltaTime);
    }

    /**
     * Sets the velocity to the vector (0,0)
     */
    public void resetVelocity () {
        velocity = defaultVelocity;
    }

    /**
     * Moves the entity after each update call
     */
    public void update() {
        if (!entity.tileCollisionOn) {
            if (!dash) {
                entity.worldX += velocity.x;
                entity.worldY += velocity.y;
            }
            else {
                moveEntity();
                entity.tileCollisionOn = false;
            }
        } else {
            resetVelocity();
        }
    }

    /**
     * Moves the entity based on velocity. Does an incremental collision check.
     * Should be used for quick and long movements
     */
    private void moveEntity () {
        float[] newPos = gp.colH.checkIncremental(entity, velocity);
        entity.worldX = newPos[0];
        entity.worldY = newPos[1];
    }

}
