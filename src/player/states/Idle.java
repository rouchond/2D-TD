package player.states;

import main.PhysicsHandler;
import main.State;
import main.KeyHandler;
import player.PlayerController;


public class Idle implements State<PlayerController> {

    KeyHandler keyH;
    PhysicsHandler physH;

    public Idle (KeyHandler keyH, PhysicsHandler physH) {
        this.keyH = keyH;
        this.physH = physH;
    }

    @Override
    public void enterState(PlayerController controller) {
        physH.resetVelocity();

        // Play Anim
    }

    @Override
    public void updateState(PlayerController controller) {
        boolean moving = keyH.leftPressed || keyH.rightPressed || keyH.upPressed || keyH.downPressed;
        if (moving) {
            controller.changeState(controller.player.moving);
        }
    }

    @Override
    public void exitState(PlayerController controller) {}
}