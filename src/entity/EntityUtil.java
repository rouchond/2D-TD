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

    /**
     *
     */
    public static Direction vectorToDirection (Vector2 vec) {
        if (vec.x > 0) {
            vec.x = 1;
        } else if (vec.x < 0) {
            vec.x = -1;
        }

        if (vec.y > 0) {
            vec.y = 1;
        } else if (vec.y < 0) {
            vec.y = -1;
        }

        if (vec.equals(new Vector2(0,-1))) {
            return Direction.UP;
        } else if (vec.equals(new Vector2(-1,-1))) {
            return Direction.UP_LEFT;
        } else if (vec.equals(new Vector2(1,-1))) {
            return Direction.UP_RIGHT;
        } else if (vec.equals(new Vector2(0,1))) {
            return Direction.DOWN;
        } else if (vec.equals(new Vector2(-1,1))) {
            return Direction.DOWN_LEFT;
        } else if (vec.equals(new Vector2(1,1))) {
            return Direction.DOWN_RIGHT;
        } else if (vec.equals(new Vector2(1,0))) {
            return Direction.RIGHT;
        } else if (vec.equals(new Vector2(-1,0))) {
            return Direction.LEFT;
        } else {
            return null;
        }
    }


}
