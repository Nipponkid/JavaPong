package net.wesleybrown.JavaPong;

import org.joml.Vector3f;

final class GameObject {

    private final Model model;

    private Vector3f position;

    private Vector3f scale;

    private String name;

    private Vector3f velocity;

    GameObject(final String name, final Model model, final Vector3f position, final Vector3f scale) {
        this.name = name;
        this.model = model;
        this.position = position;
        this.scale = scale;
        this.velocity = new Vector3f(-0.0001f, 0.0f, 0.0f);
    }

    void update(final float timesliceMS) {
        this.position.add(this.velocity);
    }

    String getName() {
        return this.name;
    }

    Vector3f getPosition() {
        return this.position;
    }

    Vector3f getScale() {
        return this.scale;
    }

    Model getModel() {
        return this.model;
    }
}
