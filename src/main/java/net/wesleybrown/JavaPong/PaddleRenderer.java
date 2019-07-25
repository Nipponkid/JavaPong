package net.wesleybrown.JavaPong;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import static org.lwjgl.opengl.GL20.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.MemoryStack;

/**
 * Renders Paddles.
 */
final class PaddleRenderer {

    /**
     * Render the given Paddle.
     * @param paddle The Paddle to render
     */
    void render(final Paddle paddle) {
        final float x = paddle.getPosition().x();
        final float y = paddle.getPosition().y();

        // The paddle's location is in the center of its model so each vertex has only up to half of the model's
        // overall width and height
        final float halfWidth = paddle.getWidth() / 2.0f;
        final float halfHeight = paddle.getHeight() / 2.0f;
        final float[] vertices = {
            // left triangle
            x - halfWidth, y + halfHeight, 0.0f,    // top-left
            x - halfWidth, y - halfHeight, 0.0f,    // bottom-left
            x + halfWidth, y + halfHeight, 0.0f,    // top-right

            // right triangle
            x - halfWidth, y - halfHeight, 0.0f,    // bottom-left
            x + halfWidth, y - halfHeight, 0.0f,    // bottom-right
            x + halfWidth, y + halfHeight, 0.0f     // top-right
        };

        final int vertexPositionBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexPositionBuffer);
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
