package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import main.State;

public class Attack implements State<PlaceholderController> {

    /**
     * The speed the enemy has while attacking
     */
    private final float attackSpeed = 15;

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
    private final float MAX_ATTACK_DURATION = 0.35f;


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
                float attackDuration = (System.nanoTime() - attackStartTime) / 1_000_000_000f;
                if (attackDuration > MAX_ATTACK_DURATION) {
                    isAttacking = false;
                } else {
                    dashAttack(controller);
                    physH.update();
                }
            } else {
                controller.changeState(controller.enemy.moving);
            }
        } else {
            knockBack(controller);
        }
    }

    @Override
    public void exitState(PlaceholderController controller) {
        controller.enemy.speed = controller.enemy.baseSpeed;
    }

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
    }

    private void dashAttack(PlaceholderController controller) {
        physH.setVelocity(dir);
        if (controller.enemy.collisionOn) {
            isKnockback = true;
            isAttacking = false;
        }
    }

    private void knockBack (PlaceholderController controller) {
        System.out.println("Knockback");
    }
}
