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
    private final Vector2 defaultVelocity;

    Entity entity;

    public PhysicsHandler (Entity entity, Vector2 velocity) {
        this.entity = entity;
        this.velocity = velocity;
        this.defaultVelocity = velocity;
    }

    public void addVelocity (float velocity) {
        entity.currentSpeed = Math.max(entity.currentSpeed + velocity, entity.maxSpeed);
    }

    private void resetVelocity () {
        velocity = defaultVelocity;
    }

    public void update () {
        entity.worldX += velocity.x;
        entity.worldY += velocity.y;
    }
}
