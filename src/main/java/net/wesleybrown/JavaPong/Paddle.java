package net.wesleybrown.JavaPong;

/**
 * A paddle in Pong.
 */
final class Paddle {

    /**
     * The x coordinate of the center of this Paddle in world space.
     */
    private float x;

    /**
     * The y coordinate of the center of this Paddle in world space.
     */
    private float y;

    /**
     * The width of this Paddle.
     */
    private float width = 10.0f;

    /**
     * The height of this Paddle.
     */
    private float height = 20.0f;

    Paddle(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }
}
