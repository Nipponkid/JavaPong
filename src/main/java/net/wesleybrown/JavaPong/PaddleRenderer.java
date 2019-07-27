package net.wesleybrown.JavaPong;

import static org.lwjgl.opengl.GL30.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FLOAT;
import static org.lwjgl.opengl.GL30.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glBufferData;
import static org.lwjgl.opengl.GL30.glDrawArrays;
import static org.lwjgl.opengl.GL30.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glUseProgram;
import static org.lwjgl.opengl.GL30.glVertexAttribPointer;

import static org.lwjgl.system.MemoryStack.stackPush;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.MemoryStack;

/**
 * Renders Paddles.
 */
final class PaddleRenderer {

    private PaddleShaderProgram shaderProgram;

    PaddleRenderer() {
        shaderProgram = new PaddleShaderProgram();
    }

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

        // Set up VAO
        final int vao = glGenVertexArrays();
        glBindVertexArray(vao);

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

        final Matrix4f transform = new Matrix4f().scale(paddle.getScale());

        // Set up uniforms
        glUseProgram(shaderProgram.getHandle());  // glGetUniformLocation requires a shader program to be being used
        final int transformLocation = glGetUniformLocation(shaderProgram.getHandle(), "transform");
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = memAllocFloat(16);
            transform.get(buffer);  // Don't need to flip because JOML does for us
            glUniformMatrix4fv(transformLocation, false, buffer);
        }

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
