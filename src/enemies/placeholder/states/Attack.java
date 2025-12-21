package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
import entity.EntityUtil;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import main.State;

public class Attack implements State<PlaceholderController> {

    /**
     * The speed the enemy has while attacking
     */
    private final float attackSpeed = 16;

    /**
     * A flag on whether the enemy is in the process of attacking
     */
    private boolean isAttacking;

    /**
     * A flag on whether the enemy is in the process of being knocked back
     */
    private boolean isKnockback;

    /**
     * Tracks when the enemy started attacking
     */
    private long attackStartTime;  // When the attack began

    /**
     * The longest the enemy can be attacking for (in seconds)
     */
    private final float MAX_ATTACK_DURATION = 0.15f;


    private Vector2 dir;

    GamePanel gp;
    PhysicsHandler physH;
    CollisionHandler colH;

    public Attack (GamePanel gp, PhysicsHandler physH, CollisionHandler colH) {
        this.gp = gp;
        this.physH = physH;
        this.colH = colH;
    }

    @Override
    public void enterState(PlaceholderController controller) {
        isAttacking = true;
        controller.enemy.speed = attackSpeed;
        getDirection(controller);
        attackStartTime = System.nanoTime();
    }

    @Override
    public void updateState(PlaceholderController controller) {
        if (!isKnockback) {
            if (isAttacking) {
                dashAttack(controller);
                }
            else {
                controller.changeState(controller.enemy.moving);
            }
        } else {
            controller.changeState(controller.enemy.knockback);
        }
    }

    @Override
    public void exitState(PlaceholderController controller) {
        controller.enemy.speed = controller.enemy.baseSpeed;
        isKnockback = false;
    }

    /**
     * Gets the direction the player is relative to the enemy
     * @param controller The state controller of the enemy
     */
    private void getDirection(PlaceholderController controller) {
        float playerX = gp.player.worldX;
        float playerY = gp.player.worldY;

        float xComp = 0, yComp = 0;

        if (controller.enemy.worldX < playerX) {
            xComp = 1;
        } else if (controller.enemy.worldX > playerX) {
            xComp = -1;
        }

        if (controller.enemy.worldY < playerY) {
            yComp = 1;
        } else if (controller.enemy.worldY > playerY) {
            yComp = -1;
        }

        dir = new Vector2(xComp, yComp);

        if (!dir.equals(new Vector2(0,0))) {
            controller.enemy.direction = EntityUtil.vectorToDirection(dir);
        }
    }

    /**
     * When in range and in the attacking state, get a snapshot of the player position
     * and move quickly in that direction for a short time, canceling the dash if the entity
     * enters a collision
     * @param controller The state controller of the enemy
     */
    private void dashAttack(PlaceholderController controller) {
        float attackDuration = (System.nanoTime() - attackStartTime) / 1_000_000_000f;
        if (attackDuration > MAX_ATTACK_DURATION) {
            isAttacking = false;
        } else {
            physH.setVelocity(dir);
            physH.dash = true;
            physH.update();
            if (controller.enemy.tileCollisionOn) {
                isKnockback = true;
                physH.dash = false;
                isAttacking = false;
            }
        }
    }

}
