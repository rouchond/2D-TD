package player.states;

import Util.Vector2;
import entity.EntityUtil;
import main.CollisionHandler;
import main.PhysicsHandler;
import main.State;
import main.KeyHandler;
import player.Player;
import player.PlayerController;

import static entity.EntityUtil.directionVectors;

public class Moving implements State<PlayerController> {

    KeyHandler keyH;
    PhysicsHandler physH;
    CollisionHandler colH;

    private Vector2 movementDir;

    public Moving (KeyHandler keyH, PhysicsHandler physH, CollisionHandler colH) {
        this.keyH = keyH;
        this.physH = physH;
        this.colH = colH;
    }

    @Override
    public void enterState(PlayerController controller) {
        // Animations go here
    }

    @Override
    public void updateState(PlayerController controller) {
        if (keyH.keyDown) {
            updateDirection(controller.player);
            move(controller.player);
            physH.update();
        }
        else {
            controller.changeState(controller.player.idle);
        }

    }

    @Override
    public void exitState(PlayerController controller) {}

    /**
     * Updates the direction the player is moving based on the key pressed
     * @param player The player object
     */
    private void updateDirection (Player player) {
        int xDir = 0, yDir = 0;
        if (keyH.upPressed) {
            yDir--;
        } else if (keyH.downPressed) {
            yDir++;
        }

        if (keyH.rightPressed) {
            xDir++;
        } else if (keyH.leftPressed) {
            xDir--;
        }

        movementDir = new Vector2(xDir, yDir);
    }

    /**
     * Sets the velocity of the player
     */
    private void move (Player player) {
        physH.setVelocity(movementDir.normalize());
    }
}
