package net.wesleybrown.JavaPong;

import org.joml.Vector3f;

final class Ball {

    private final float width = 1.0f;

    private final float height = 1.0f;

    private Vector3f position;

    Ball(final float x, final float y) {
        position = new Vector3f(x, y, 0.0f);
    }

    Vector3f getPosition() {
        return position;
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }
}
