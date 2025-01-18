package player.states;

import entity.EntityUtil;
import main.State;
import main.KeyHandler;
import player.Player;
import player.PlayerController;

public class Moving implements State<PlayerController> {

    KeyHandler keyH;

    public Moving (KeyHandler keyH) {
        this.keyH = keyH;
    }

    @Override
    public void enterState(PlayerController controller) {
        // Animations go here
    }

    @Override
    public void updateState(PlayerController controller) {
        if (controller.player.currentSpeed != 0 && !keyH.keyDown) {
            updateDirection(controller.player);
            move();
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
     *
     */
    private void move (Player player) {
        switch (player.direction) {
            case UP:

                break;
        }
    }
}
