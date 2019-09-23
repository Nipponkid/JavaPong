package net.wesleybrown.javapong;

import org.joml.Vector3f;

final class GameObject {

    private Vector3f position;

    private Vector3f scale;

    private String name;

    private Vector3f velocity;

    private final GameObjectRenderer renderer;

    GameObject(final String name, final Model model, final Vector3f position, final Vector3f scale) {
        this.name = name;
        this.position = position;
        this.scale = scale;
        this.velocity = new Vector3f(0.0f, 0.0f, 0.0f);
        renderer = new GameObjectRenderer(model);
    }

    void update(final float timesliceMS) {
        this.position.add(this.velocity);
    }

    Vector3f position() {
        return this.position;
    }

    Vector3f scale() {
        return this.scale;
    }

    void setVelocity(final Vector3f velocity) {
        this.velocity = velocity;
    }

    void render() {
        renderer.render(position, scale);
    }

    boolean isCollidingWith(final GameObject other) {
        final float THIS_HALF_WIDTH = scale.x() / 2.0f;
        final float THIS_HALF_HEIGHT = scale.y() / 2.0f;

        final float OTHER_HALF_WIDTH = other.scale.x() / 2.0f;
        final float OTHER_HALF_HEIGHT = other.scale.y() / 2.0f;

        final float X_MAX = position.x() + THIS_HALF_WIDTH;
        final float X_MIN = position.x() - THIS_HALF_WIDTH;

        final float OTHER_X_MAX = other.position.x() + OTHER_HALF_WIDTH;
        final float OTHER_X_MIN = other.position.x() - OTHER_HALF_WIDTH;

        final float Y_MAX = position.y() + THIS_HALF_HEIGHT;
        final float Y_MIN = position.y() - THIS_HALF_HEIGHT;

        final float OTHER_Y_MAX = other.position.y() + OTHER_HALF_HEIGHT;
        final float OTHER_Y_MIN = other.position.y() - OTHER_HALF_HEIGHT;

        final boolean xCollision = (X_MAX >= OTHER_X_MIN) && (X_MIN <= OTHER_X_MAX);
        final boolean yCollision = (Y_MAX >= OTHER_Y_MIN) && (Y_MIN <= OTHER_Y_MAX);

        return (xCollision && yCollision);
    }
}
