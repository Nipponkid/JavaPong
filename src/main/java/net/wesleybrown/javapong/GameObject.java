package net.wesleybrown.javapong;

import java.util.Optional;

import org.joml.Vector3f;

final class GameObject {

    private Vector3f position;

    private Vector3f scale;

    private String name;

    private Vector3f velocity;

    private Material material;

    private final GameObjectRenderer renderer;

    GameObject(final String name, final Model model, final Vector3f position, final Vector3f scale, final Material material) {
        this.name = name;
        this.position = position;
        this.scale = scale;
        this.velocity = new Vector3f(0.0f, 0.0f, 0.0f);
        this.material = material;
        renderer = new GameObjectRenderer(model, material);
    }

    void update(final float timesliceMS) {
        final Vector3f scaledVelocity = new Vector3f();
        velocity.mul(timesliceMS, scaledVelocity);
        this.position.add(scaledVelocity);
    }

    void setVelocity(final Vector3f velocity) {
        this.velocity = velocity;
    }

    void render(final float deltaTime) {
        final Vector3f extrapolatedVelocity = new Vector3f(velocity).mul(deltaTime);
        final Vector3f extrapolatedPosition = new Vector3f(position).add(extrapolatedVelocity);
        renderer.render(extrapolatedPosition, scale);
    }

    boolean isCollidingWith(final GameObject other) {
        final Rectangle thisBoundingBox = new Rectangle(position, scale.x(), scale.y());
        final Rectangle otherBoundingBox = new Rectangle(other.position, other.scale.x(), other.scale.y());
        return thisBoundingBox.intersectsWith(otherBoundingBox);
    }

    void respondToCollisionWith(final GameObject other) {
        final Rectangle thisBoundingBox = new Rectangle(position, scale.x(), scale.y());
        final Rectangle otherBoundingBox = new Rectangle(other.position, other.scale.x(), other.scale.y());
        final Optional<Rectangle> overlap = thisBoundingBox.intersectionWith(otherBoundingBox);

        if (overlap.isPresent()) {
            final Rectangle overlapRec = overlap.get();
            if (overlapRec.width() >= overlapRec.height()) {
                if (position.y() < other.position.y()) {
                    final Vector3f temp = new Vector3f(0.0f, -1.0f, 0.f);
                    temp.mul(overlapRec.width()).mul(scale.y());
                    position.add(temp);
                    velocity = new Vector3f(velocity.x(), -velocity.y(), velocity.z());
                } else {
                    final Vector3f temp = new Vector3f(0.0f, 1.0f, 0.f);
                    temp.mul(overlapRec.width()).mul(scale.y());
                    position.add(temp);
                    velocity = new Vector3f(velocity.x(), -velocity.y(), velocity.z());
                }
            } else {
                if (position.x() < other.position.x()) {
                    final Vector3f temp = new Vector3f(-1.0f, 0.0f, 0.0f);
                    temp.mul(overlapRec.height()).mul(scale.x());
                    position.add(temp);
                    velocity = new Vector3f(-velocity.x(), velocity.y(), velocity.z());
                } else {
                    final Vector3f temp = new Vector3f(1.0f, 0.0f, 0.0f);
                    temp.mul(overlapRec.height()).mul(scale.x());
                    position.add(temp);
                    velocity = new Vector3f(-velocity.x(), velocity.y(), velocity.z());
                }
            }
        }
    }
}
