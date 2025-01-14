package player.states;

import main.State;
import main.KeyHandler;
import player.PlayerController;


public class Idle implements State<PlayerController> {

    KeyHandler keyH;

    public Idle (KeyHandler keyH) {
        this.keyH = keyH;
    }

    @Override
    public void enterState(PlayerController controller) {
        // Play Anim
    }

    @Override
    public void updateState(PlayerController controller) {
        boolean moving  = keyH.leftPressed || keyH.rightPressed || keyH.upPressed || keyH.downPressed;
        if (moving) {
            controller.changeState(controller.player.moving);
        }
    }

    @Override
    public void exitState(PlayerController controller) {}
}