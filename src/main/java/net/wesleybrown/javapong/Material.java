package net.wesleybrown.javapong;

final class Material {

    private static final float[] redBuffer = {
            1.0f, 0.0f, 0.0f, 1.0f, // top-left
            1.0f, 0.0f, 0.0f, 1.0f, // bottom-left
            1.0f, 0.0f, 0.0f, 1.0f, // top-right,

            1.0f, 0.0f, 0.0f, 1.0f, // bottom-left
            1.0f, 0.0f, 0.0f, 1.0f, // bottom-right
            1.0f, 0.0f, 0.0f, 1.0f, // top-right
    };

    private static final float[] greenBuffer = {
            0.0f, 1.0f, 0.0f, 1.0f, // top-left
            0.0f, 1.0f, 0.0f, 1.0f, // bottom-left
            0.0f, 1.0f, 0.0f, 1.0f, // top-right,

            0.0f, 1.0f, 0.0f, 1.0f, // bottom-left
            0.0f, 1.0f, 0.0f, 1.0f, // bottom-right
            0.0f, 1.0f, 0.0f, 1.0f, // top-right
    };

    private static final float[] blueBuffer = {
            0.0f, 0.0f, 1.0f, 1.0f, // top-left
            0.0f, 0.0f, 1.0f, 1.0f, // bottom-left
            0.0f, 0.0f, 1.0f, 1.0f, // top-right,

            0.0f, 0.0f, 1.0f, 1.0f, // bottom-left
            0.0f, 0.0f, 1.0f, 1.0f, // bottom-right
            0.0f, 0.0f, 1.0f, 1.0f, // top-right
    };

    private static final float[] blackBuffer = {
            0.0f, 0.0f, 0.0f, 0.0f, // top-left
            0.0f, 0.0f, 0.0f, 0.0f, // bottom-left
            0.0f, 0.0f, 0.0f, 0.0f, // top-right

            0.0f, 0.0f, 0.0f, 0.0f, // bottom-left
            0.0f, 0.0f, 0.0f, 0.0f, // bottom-right
            0.0f, 0.0f, 0.0f, 0.0f  // top-right
    };

    final static Material RED = new Material(redBuffer);
    final static Material GREEN = new Material(greenBuffer);
    final static Material BLUE = new Material(blueBuffer);
    final static Material BLACK = new Material(blackBuffer);

    private final float[] colorBuffer;

    Material(final float[] colorBuffer) {
        this.colorBuffer = colorBuffer;
    }

    float[] bufferData() {
        return colorBuffer;
    }

}
