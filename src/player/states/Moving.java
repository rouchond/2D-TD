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
            colH.checkTile(controller.player);
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
        if (keyH.upPressed) {
            if (keyH.rightPressed) {
                player.direction = EntityUtil.Direction.UP_RIGHT;
            } else if (keyH.leftPressed) {
                player.direction = EntityUtil.Direction.UP_LEFT;
            } else {
                player.direction = EntityUtil.Direction.UP;
            }

        } else if (keyH.downPressed) {
            if (keyH.rightPressed) {
                player.direction = EntityUtil.Direction.DOWN_RIGHT;
            } else if (keyH.leftPressed) {
                player.direction = EntityUtil.Direction.DOWN_LEFT;
            } else {
                player.direction = EntityUtil.Direction.DOWN;
            }

        } else if (keyH.rightPressed) {
            player.direction = EntityUtil.Direction.RIGHT;
        } else if (keyH.leftPressed) {
            player.direction = EntityUtil.Direction.LEFT;
        }
    }

    /**
     * Sets the velocity of the player
     */
    private void move (Player player) {
        physH.setVelocity(directionVectors.get(player.direction).normalize());
    }
}
