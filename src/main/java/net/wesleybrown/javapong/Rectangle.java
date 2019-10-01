package net.wesleybrown.javapong;

import java.util.Optional;

import org.joml.Vector3f;

/**
 * A rectangle.
 */
final class Rectangle {

    private final float xMin;
    private final float xMax;

    private final float yMin;
    private final float yMax;

    private final float width;
    private final float height;

    Rectangle(final Vector3f position, final float width, final float height) throws IllegalArgumentException {
        if (width <= 0.0f || height <= 0.0f) {
            throw new IllegalArgumentException("width and height must be > 0");
        }
        final float HALF_WIDTH = width / 2.0f;
        final float HALF_HEIGHT = height / 2.0f;

        xMin = position.x() - HALF_WIDTH;
        xMax = position.x() + HALF_WIDTH;

        yMin = position.y() - HALF_HEIGHT;
        yMax = position.y() + HALF_HEIGHT;

        this.width = width;
        this.height = height;
    }

    float width() {
        return width;
    }

    float height() {
        return height;
    }

    /**
     * Determines whether or not this Rectangle intersects with another.
     * @param other the other Rectangle
     * @return true if this Rectangle intersects with other; false otherwise
     */
    boolean intersectsWith(final Rectangle other) {
        return hasXAxisOverlapWith(other) && hasYAxisOverlapWith(other);
    }

    Optional<Rectangle> intersectionWith(final Rectangle other) {
        final float xOverlap = Math.min(xMax, other.xMax) - Math.max(xMin, other.xMin);
        final float yOverlap = Math.min(yMax, other.yMax) - Math.max(yMin, other.yMin);

        if (xOverlap <= 0.0f || yOverlap <= 0.0f) {
            return Optional.empty();
        } else {
            return Optional.of(new Rectangle(new Vector3f(), xOverlap, yOverlap));
        }
    }

    private boolean hasXAxisOverlapWith(final Rectangle other) {
        return (xMax >= other.xMin) && (xMin <= other.xMax);
    }

    private boolean hasYAxisOverlapWith(final Rectangle other) {
        return (yMax >= other.yMin) && (yMin <= other.yMax);
    }
}
