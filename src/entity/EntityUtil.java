package entity;

import Util.Vector2;

import java.util.HashMap;

public class EntityUtil {
    /**
     * The 8 possible directions of the player
     */
    public enum Direction {
        UP,
        UP_LEFT,
        UP_RIGHT,
        DOWN,
        DOWN_LEFT,
        DOWN_RIGHT,
        LEFT,
        RIGHT
    }

    /**
     * The vectors associated with each direction
     */
    public static final HashMap<Direction, Vector2> directionVectors;
    static {
        HashMap<Direction, Vector2> map = new HashMap<>();
        map.put(Direction.UP, new Vector2(0,-1));
        map.put(Direction.UP_LEFT, new Vector2(-1,-1));
        map.put(Direction.UP_RIGHT, new Vector2(1,-1));
        map.put(Direction.DOWN, new Vector2(0,1));
        map.put(Direction.DOWN_LEFT, new Vector2(-1,1));
        map.put(Direction.DOWN_RIGHT, new Vector2(1,1));
        map.put(Direction.RIGHT, new Vector2(1,0));
        map.put(Direction.LEFT, new Vector2(-1,0));
        directionVectors = map;
    }


}
