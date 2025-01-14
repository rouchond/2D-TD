package entity;

import main.State;

public interface EntityController<T extends EntityController<T>> {

    /**
     * The state that the entity is in
     */
    public State<?> currentState = null;




    /**
     * Initializes the state of the entity
     * @param startState The state to initialize the entity with
     */
    void setupState(State<T> startState);

    /**
     * Cleans up the current state, then enters a new one
     * @param newState The new state to enter, enterState() is immediately called
     */
    void changeState(State<T> newState);

    /**
     * The logic of the entity that needs to be updated every frame
     */
    public void update();

}