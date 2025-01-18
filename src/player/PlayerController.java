package player;

import entity.EntityController;
import main.CollisionHandler;
import main.KeyHandler;
import main.State;

public class PlayerController implements EntityController<PlayerController> {

    public Player player;
    CollisionHandler colH;
    public State<PlayerController> currentState;

    public PlayerController (Player player) {
        this.player = player;
    }


    @Override
    public void setupState(State<PlayerController> startState) {
        currentState = startState;
        currentState.enterState(this);
    }

    @Override
    public void changeState(State<PlayerController> newState) {
        currentState.exitState(this);
        currentState = newState;
        currentState.enterState(this);
    }

    public void update() {currentState.updateState(this);}

}