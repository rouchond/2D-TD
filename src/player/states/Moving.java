package player.states;

import main.State;
import main.KeyHandler;
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
        if (controller.player.currentSpeed != 0) {
            getDirection();
            move();
        }
        else {
            controller.changeState(controller.player.idle);
        }

    }

    @Override
    public void exitState(PlayerController controller) {}

    /**
     *
     */
    private void getDirection () {
        // Something
    }

    /**
     *
     */
    private void move () {
        // Something
    }
}
