package net.wesleybrown.JavaPong;

import org.joml.Vector3f;

/**
 * A paddle in Pong.
 */
final class Paddle {

    /**
     * The width of this Paddle.
     */
    private float width = 0.25f;

    /**
     * The height of this Paddle.
     */
    private float height = 0.5f;

    /**
     * The position of the center of this Paddle in world space.
     */
    private Vector3f position;

    Paddle() {
        new Paddle(new Vector3f());
    }

    Paddle(final Vector3f position) {
        this.position = new Vector3f(position);
    }

    Vector3f getPosition() {
        return new Vector3f(this.position);
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }
}
