package player;

import Util.Vector2;
import enemies.placeholder.Enemy01;
import main.KeyHandler;

import java.awt.*;

public class MeleeAttack {

    // Attack constants
    static final long ATTACK_COOLDOWN = 350_000_000L; // 0.35s in nanos
    static final float ATTACK_RANGE = 30f;
    static final int ATTACK_WIDTH = 28;
    static final int ATTACK_HEIGHT = 28;
    static final int ATTACK_DAMAGE = 5;

    // Attack state
    private long lastAttackTime = 0;
    private boolean attackActive;
    private Rectangle attackRect;

    private final Player player;
    private final KeyHandler keyH;

    public MeleeAttack(Player player, KeyHandler keyH) {
        this.player = player;
        this.keyH = keyH;
    }

    /**
     * Checks for edge-triggered click and performs attack if cooldown allows
     */
    public void update() {
        boolean clickedThisFrame = keyH.mousePressed && !keyH.previousMousePressed;
        if (clickedThisFrame && canAttack(lastAttackTime, System.nanoTime(), ATTACK_COOLDOWN)) {
            performAttack();
        } else {
            attackActive = false;
        }
    }

    /**
     * Executes a melee attack: places a hitbox in the facing direction and checks against enemies
     */
    private void performAttack() {
        lastAttackTime = System.nanoTime();
        attackActive = true;

        float playerCenterX = player.worldX + player.solidArea.x + player.solidArea.width / 2f;
        float playerCenterY = player.worldY + player.solidArea.y + player.solidArea.height / 2f;

        attackRect = computeAttackRect(playerCenterX, playerCenterY, player.facingDir, ATTACK_RANGE, ATTACK_WIDTH, ATTACK_HEIGHT);

        for (Enemy01 enemy : player.gp.enemyH.getEnemies()) {
            Rectangle enemyWorldRect = new Rectangle(
                    Math.round(enemy.worldX) + enemy.solidArea.x,
                    Math.round(enemy.worldY) + enemy.solidArea.y,
                    enemy.solidArea.width,
                    enemy.solidArea.height
            );
            if (attackRect.intersects(enemyWorldRect)) {
                enemy.health -= ATTACK_DAMAGE;
            }
        }
    }

    /**
     * Draws attack debug visuals (yellow hitbox when active)
     */
    public void drawDebug(Graphics2D g2, main.Camera camera) {
        if (attackActive && attackRect != null) {
            g2.setColor(Color.yellow);
            g2.drawRect(
                    camera.toScreenX(attackRect.x),
                    camera.toScreenY(attackRect.y),
                    attackRect.width,
                    attackRect.height
            );
        }
    }

    // --- Static helpers (testable without instance) ---

    /**
     * Returns true if enough time has elapsed since the last attack
     */
    static boolean canAttack(long lastAttackTime, long currentTime, long cooldownNanos) {
        return (currentTime - lastAttackTime) >= cooldownNanos;
    }

    /**
     * Computes the attack hitbox Rectangle in world coordinates, centered at
     * playerCenter + facingDir * range
     */
    static Rectangle computeAttackRect(float playerCenterX, float playerCenterY, Vector2 facingDir, float range, int width, int height) {
        float cx = playerCenterX + facingDir.x * range;
        float cy = playerCenterY + facingDir.y * range;
        return new Rectangle(Math.round(cx) - width / 2, Math.round(cy) - height / 2, width, height);
    }
}
