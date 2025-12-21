package enemies.placeholder.states;

import Util.Vector2;
import enemies.placeholder.PlaceholderController;
import entity.EntityUtil;
import main.CollisionHandler;
import main.GamePanel;
import main.PhysicsHandler;
import main.State;
import tile.TileManager;


public class Knockback implements State<PlaceholderController> {

    /**
     * The longest the enemy can be sent back for (in seconds)
     */
    private final float MAX_KNOCKBACK_DURATION = 0.35f;

    /**
     * Tracks when the enemy started attacking
     */
    private long knockBackStartTime;

    private Vector2 dir;

    GamePanel gp;
    PhysicsHandler physH;
    CollisionHandler colH;
    TileManager tileM;

    public Knockback (GamePanel gp, PhysicsHandler physH, CollisionHandler colH, TileManager tileM) {
        this.gp = gp;
        this.physH = physH;
        this.colH = colH;
        this.tileM = tileM;
    }
    @Override
    public void enterState(PlaceholderController controller) {
        knockBackStartTime = System.nanoTime();
        dir = EntityUtil.directionVectors.get(controller.enemy.direction).opposite();
        physH.setVelocity(dir);
    }

    @Override
    public void updateState(PlaceholderController controller) {
        controller.enemy.tileCollisionOn = false;
        float knockbackDuration = (System.nanoTime() - knockBackStartTime) / 1_000_000_000f;
        if (knockbackDuration < MAX_KNOCKBACK_DURATION) {
            physH.update();
        }
        else {
            controller.changeState(controller.enemy.moving);
        }
    }

    @Override
    public void exitState(PlaceholderController controller) {

    }
}
