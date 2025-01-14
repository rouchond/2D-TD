package Util;

public class Vector2 {

    /**
     * The x component of the vector
     */
    public float x;

    /**
     * The y component of the vector
     */
    public float y;

    public Vector2 (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double normalize () {
        return (1)/(Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));
    }
}
