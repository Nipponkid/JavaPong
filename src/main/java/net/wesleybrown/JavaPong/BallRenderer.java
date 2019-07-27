package net.wesleybrown.JavaPong;

import static org.lwjgl.opengl.GL30.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FLOAT;
import static org.lwjgl.opengl.GL30.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBufferData;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDrawArrays;
import static org.lwjgl.opengl.GL30.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glUseProgram;
import static org.lwjgl.opengl.GL30.glVertexAttribPointer;

import static org.lwjgl.system.MemoryStack.stackPush;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;

import org.lwjgl.system.MemoryStack;

/**
 * Renders Balls.
 */
final class BallRenderer {

    private static float[] vertices = {
            // left triangle
            -1.0f,  1.0f,  0.0f,    // top-left
            -1.0f, -1.0f,  0.0f,    // bottom-left
            1.0f,  1.0f,  0.0f,    // top-right

            // right triangle
            -1.0f, -1.0f,  0.0f,    // bottom-left
            1.0f, -1.0f,  0.0f,    // bottom-right
            1.0f,  1.0f,  0.0f     // top-right
    };

    private PaddleShaderProgram shaderProgram;    // The same shader is used for the ball and paddles

    BallRenderer() {
        shaderProgram = new PaddleShaderProgram();
    }

    void render(final Ball ball) {
        final float x = ball.getPosition().x();
        final float y = ball.getPosition().y();

        final float halfWidth = ball.getWidth() / 2.0f;
        final float halfHeight = ball.getHeight() / 2.0f;

        final int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        final int vbo = glGenBuffers();
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = memAllocFloat(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }

        final Matrix4f transform = new Matrix4f().scale(0.05f);

        glUseProgram(shaderProgram.getHandle());
        final int transformLocation = glGetUniformLocation(shaderProgram.getHandle(), "transform");
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = memAllocFloat(16);
            transform.get(buffer);
            glUniformMatrix4fv(transformLocation, false, buffer);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
