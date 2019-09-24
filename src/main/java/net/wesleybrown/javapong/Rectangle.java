package net.wesleybrown.javapong;

import org.joml.Vector3f;

/**
 * A rectangle.
 */
final class Rectangle {

    private final float xMin;
    private final float xMax;

    private final float yMin;
    private final float yMax;

    Rectangle(final Vector3f position, final float width, final float height) {
        final float HALF_WIDTH = width / 2.0f;
        final float HALF_HEIGHT = height / 2.0f;

        xMin = position.x() - HALF_WIDTH;
        xMax = position.x() + HALF_WIDTH;

        yMin = position.y() - HALF_HEIGHT;
        yMax = position.y() + HALF_HEIGHT;
    }

    boolean intersectsWith(final Rectangle other) {
        return false;
    }
}
