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
    public final Vector2 defaultVelocity = new Vector2(0,0);

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
     *
     */
    public void resetVelocity () {
        velocity = defaultVelocity;
    }

    /**
     *
     */
    public void update () {
        entity.worldX += velocity.x;
        entity.worldY += velocity.y;
    }
}
