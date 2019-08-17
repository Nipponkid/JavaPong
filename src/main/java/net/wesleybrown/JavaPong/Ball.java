package net.wesleybrown.JavaPong;

import org.joml.Vector3f;

/**
 * A ball in Pong.
 */
final class Ball {

    private final float width = 1.0f;

    private final float height = 1.0f;

    private Vector3f position;

    private float uniformScale;

    private Vector3f velocity;

    private final Model model = Model.SQUARE;

    Ball(final float x, final float y, final float uniformScale) {
        this.position = new Vector3f(x, y, 0.0f);
        this.uniformScale = uniformScale;
        this.velocity = new Vector3f(0.0000001f, 0.0f, 0.0f);
    }

    Vector3f getPosition() {
        return this.position;
    }

    float getWidth() {
        return this.width;
    }

    float getHeight() {
        return this.height;
    }

    Model getModel() {
        return this.model;
    }

    /**
     * Set the uniform scale for this Ball.
     * @param uniformScale The uniform scale to set this Ball's to
     */
    void setUniformScale(final float uniformScale) {
        this.uniformScale = uniformScale;
    }

    /**
     * Get the uniform scale for this Ball.
     * @return The uniform scale for this Ball
     */
    float getUniformScale() {
        return this.uniformScale;
    }

    Vector3f getVelocity() {
        return this.velocity;
    }

    void update(final long timesliceMS) {
        this.position.add(velocity);
    }
}
