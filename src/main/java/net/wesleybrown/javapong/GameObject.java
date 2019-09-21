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
}
