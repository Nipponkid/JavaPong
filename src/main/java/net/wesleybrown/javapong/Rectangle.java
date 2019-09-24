package net.wesleybrown.javapong;

import org.joml.Vector3f;

/**
 * A rectangle.
 */
final class Rectangle {

    static final Rectangle EMPTY = new Rectangle(new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f);

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
        final boolean isCollidingOverXAxis = (xMax >= other.xMin) && (xMin <= other.xMax);
        final boolean isCollidingOverYAxis = (yMax >= other.yMin) && (yMin <= other.yMax);
        return (isCollidingOverXAxis && isCollidingOverYAxis);
    }

    Rectangle intersectionWith(final Rectangle other) {
        return Rectangle.EMPTY;
    }
}
