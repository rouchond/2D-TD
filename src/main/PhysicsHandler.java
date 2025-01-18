package main;

import Util.Vector2;
import entity.Entity;

public class PhysicsHandler {

    /**
     * The current velocity of the entity
     */
    public Vector2 velocity;

    /**
     * The velocity of the entity without forces applied
     */
    private final Vector2 defaultVelocity = new Vector2(0,0);

    Entity entity;
    GamePanel gp;

    public PhysicsHandler (Entity entity, GamePanel gp) {
        this.entity = entity;
        this.velocity = defaultVelocity;
        this.gp = gp;
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
     * Checks collisions to see if a player can move based on the velocity
     */
    public void update () {
        if (!entity.collisionOn) {
            entity.worldX += velocity.x;
            entity.worldY += velocity.y;
        } else {
            resetVelocity();
        }
    }
}
