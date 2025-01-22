package enemies.placeholder;

import entity.EntityController;
import main.GamePanel;
import main.State;
import tile.TileManager;

public class PlaceholderController implements EntityController<PlaceholderController> {

    public Enemy01 enemy;
    GamePanel gp;

    public PlaceholderController (GamePanel gp) {
        this.gp = gp;
    }

    public State<PlaceholderController> currentState;
    @Override
    public void setupState(State<PlaceholderController> startState) {
        currentState = startState;
        currentState.enterState(this);
    }

    @Override
    public void changeState(State<PlaceholderController> newState) {
        currentState.exitState(this);
        currentState = newState;
        currentState.enterState(this);
    }

    @Override
    public void update() {
        currentState.updateState(this);
    }
}
