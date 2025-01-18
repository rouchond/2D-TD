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

    public Vector2 normalize () {
        float mag = (float) ((Math.sqrt(x * x + y * y)));
        if (mag != 0) {
            return new Vector2(x / mag, y / mag);
        } else {
            return this;
        }

    }
}
