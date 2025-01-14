package main;

import entity.EntityController;

public interface State<T extends EntityController<T>> {

    /**
     * Sets up the state to then call update continuously
     * @param controller The state controller of the entity.
     */
    void enterState(T controller);

    /**
     * Updates the state of the entity. This is where the bulk of the entity's logic lies
     * @param controller The state controller of the entity.
     */
    void updateState(T controller);

    /**
     * Cleans up the state before transitioning out into another state
     * @param controller The state controller of the entity.
     */
    void exitState(T controller);


}
