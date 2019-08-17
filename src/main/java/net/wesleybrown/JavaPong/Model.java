package net.wesleybrown.JavaPong;

final class Model {

    /**
     * The Model for a unit square.
     */
    static final Model SQUARE = new Model(new float[] {
            // left triangle
            -1.0f,  1.0f,  0.0f,    // top-left
            -1.0f, -1.0f,  0.0f,    // bottom-left
            1.0f,  1.0f,  0.0f,    // top-right

            // right triangle
            -1.0f, -1.0f,  0.0f,    // bottom-left
            1.0f, -1.0f,  0.0f,    // bottom-right
            1.0f,  1.0f,  0.0f     // top-right
    });

    private final float[] vertices;

    Model(final float[] vertices) throws IllegalArgumentException {
        if ((vertices.length % 3) != 0) {
            throw new IllegalArgumentException("each vertex must have exactly three properties");
        }

        this.vertices = vertices;
    }

    float[] getVertices() {
        return this.vertices;
    }
}
