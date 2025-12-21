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

    /**
     * Returns a normalized vector
     */
    public Vector2 normalize () {
        float mag = (float) ((Math.sqrt(x * x + y * y)));
        if (mag != 0) {
            return new Vector2(x / mag, y / mag);
        } else {
            return this;
        }

    }

    /**
     * Returns a vector pointing in the opposite direction
     * @return
     */
    public Vector2 opposite () {return new Vector2(-x, -y);}

    /**
     * Returns true if the object passed is a vector with the same x & y component
     * @param o The object we're comparing the vector to
     */
    @Override
    public boolean equals (Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Vector2 vec)) {
            return  false;
        }
        else {
            return x == vec.x && y == vec.y;
        }
    }
}
