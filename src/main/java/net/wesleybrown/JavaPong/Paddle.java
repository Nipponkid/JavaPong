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

    private float scale;

    /**
     * The position of the center of this Paddle in world space.
     */
    private Vector3f position;

    /**
     * This Paddle's current velocity.
     */
    private Vector3f velocity = new Vector3f();

    static Paddle atPosition(final Vector3f position) {
        return Paddle.atPositionAtScale(position, 1.0f);
    }

    static Paddle atPositionAtScale(final Vector3f position, final float scale) {
        return new Paddle(position, scale);
    }

    private Paddle(final Vector3f position, final float scale) {
        this.position = new Vector3f(position);
        this.scale = scale;
    }

    Vector3f getPosition() {
        return new Vector3f(this.position);
    }

    void setPosition(final Vector3f position) {
        this.position = position;
    }

    Vector3f getVelocity() {
        return this.velocity;
    }

    void setVelocity(final Vector3f velocity) {
        this.velocity = velocity;
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }

    float getScale() {
        return scale;
    }

    void update(final float timesliceMS) {
        position.add(velocity);
    }
}
