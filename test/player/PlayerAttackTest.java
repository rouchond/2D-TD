package player;

import Util.Vector2;
import entity.EntityUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerAttackTest {

    // --- Facing direction tests ---

    @Test
    void facingDirectionPointsRight() {
        // Player screen center at (400, 300), mouse at (500, 300) → facing right
        Vector2 dir = Player.computeFacingDirection(400, 300, 500, 300);
        assertEquals(1f, dir.x, 0.001f);
        assertEquals(0f, dir.y, 0.001f);
    }

    @Test
    void facingDirectionPointsLeft() {
        Vector2 dir = Player.computeFacingDirection(400, 300, 200, 300);
        assertEquals(-1f, dir.x, 0.001f);
        assertEquals(0f, dir.y, 0.001f);
    }

    @Test
    void facingDirectionPointsUp() {
        Vector2 dir = Player.computeFacingDirection(400, 300, 400, 100);
        assertEquals(0f, dir.x, 0.001f);
        assertEquals(-1f, dir.y, 0.001f);
    }

    @Test
    void facingDirectionPointsDown() {
        Vector2 dir = Player.computeFacingDirection(400, 300, 400, 500);
        assertEquals(0f, dir.x, 0.001f);
        assertEquals(1f, dir.y, 0.001f);
    }

    @Test
    void facingDirectionDiagonalIsNormalized() {
        // Mouse directly diagonal from player center
        Vector2 dir = Player.computeFacingDirection(400, 300, 500, 400);
        float len = dir.length();
        assertEquals(1f, len, 0.001f);
    }

    @Test
    void facingDirectionAtMouseOnPlayerReturnsZero() {
        // Mouse exactly on player center → zero vector (can't normalize)
        Vector2 dir = Player.computeFacingDirection(400, 300, 400, 300);
        assertEquals(0f, dir.x, 0.001f);
        assertEquals(0f, dir.y, 0.001f);
    }

    // --- vectorToDirection with copy tests ---

    @Test
    void facingRightMapsToDirectionRight() {
        Vector2 facing = new Vector2(1f, 0f);
        EntityUtil.Direction d = EntityUtil.vectorToDirection(new Vector2(facing.x, facing.y));
        assertEquals(EntityUtil.Direction.RIGHT, d);
    }

    @Test
    void facingUpLeftMapsToDirectionUpLeft() {
        Vector2 facing = new Vector2(-0.707f, -0.707f);
        EntityUtil.Direction d = EntityUtil.vectorToDirection(new Vector2(facing.x, facing.y));
        assertEquals(EntityUtil.Direction.UP_LEFT, d);
    }

    // --- Attack cooldown tests ---

    @Test
    void cannotAttackBeforeCooldownExpires() {
        long cooldown = 350_000_000L; // 0.35s in nanos
        long lastAttack = 1_000_000_000L;
        long now = lastAttack + 200_000_000L; // 0.2s later
        assertFalse(MeleeAttack.canAttack(lastAttack, now, cooldown));
    }

    @Test
    void canAttackAfterCooldownExpires() {
        long cooldown = 350_000_000L;
        long lastAttack = 1_000_000_000L;
        long now = lastAttack + 400_000_000L; // 0.4s later
        assertTrue(MeleeAttack.canAttack(lastAttack, now, cooldown));
    }

    @Test
    void canAttackExactlyAtCooldownBoundary() {
        long cooldown = 350_000_000L;
        long lastAttack = 1_000_000_000L;
        long now = lastAttack + cooldown; // exactly at boundary
        assertTrue(MeleeAttack.canAttack(lastAttack, now, cooldown));
    }

    @Test
    void canAttackWhenNeverAttackedBefore() {
        long cooldown = 350_000_000L;
        long lastAttack = 0L; // never attacked
        long now = 5_000_000_000L; // System.nanoTime() returns large values in practice
        assertTrue(MeleeAttack.canAttack(lastAttack, now, cooldown));
    }

    // --- Attack hitbox positioning tests ---

    @Test
    void attackHitboxPositionedToTheRight() {
        Rectangle rect = MeleeAttack.computeAttackRect(100, 100, new Vector2(1, 0), 30, 28, 28);
        // Center of rect should be at (100 + 30, 100) = (130, 100)
        assertEquals(130 - 14, rect.x);
        assertEquals(100 - 14, rect.y);
        assertEquals(28, rect.width);
        assertEquals(28, rect.height);
    }

    @Test
    void attackHitboxPositionedUpward() {
        Rectangle rect = MeleeAttack.computeAttackRect(100, 100, new Vector2(0, -1), 30, 28, 28);
        // Center at (100, 100 - 30) = (100, 70)
        assertEquals(100 - 14, rect.x);
        assertEquals(70 - 14, rect.y);
    }

    @Test
    void attackHitboxPositionedDiagonal() {
        Vector2 facing = new Vector2(1, 1).normalize();
        Rectangle rect = MeleeAttack.computeAttackRect(100, 100, facing, 30, 28, 28);
        float expectedCX = 100 + facing.x * 30;
        float expectedCY = 100 + facing.y * 30;
        assertEquals(Math.round(expectedCX) - 14, rect.x);
        assertEquals(Math.round(expectedCY) - 14, rect.y);
    }

    // --- Hit detection tests ---

    @Test
    void attackHitboxIntersectsEnemy() {
        Rectangle attack = new Rectangle(90, 90, 28, 28); // covers 90-118 on both axes
        Rectangle enemy = new Rectangle(100, 100, 32, 32); // covers 100-132
        assertTrue(attack.intersects(enemy));
    }

    @Test
    void attackHitboxMissesEnemy() {
        Rectangle attack = new Rectangle(50, 50, 28, 28); // covers 50-78
        Rectangle enemy = new Rectangle(200, 200, 32, 32); // covers 200-232
        assertFalse(attack.intersects(enemy));
    }

    @Test
    void attackHitboxEdgeOverlap() {
        Rectangle attack = new Rectangle(0, 0, 28, 28); // covers 0-28
        Rectangle enemy = new Rectangle(27, 27, 32, 32); // overlaps by 1px
        assertTrue(attack.intersects(enemy));
    }
}