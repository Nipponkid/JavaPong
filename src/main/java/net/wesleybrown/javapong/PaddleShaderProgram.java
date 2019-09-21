package net.wesleybrown.javapong;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

/**
 * A shader program that Paddles use.
 */
final class PaddleShaderProgram {

    private static final String vertexShaderSource = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n\n"
            + "uniform mat4 transform;\n" + "void main()\n" + "{\n"
            + "    gl_Position = transform * vec4(aPos, 1.0f);\n" + "}\n";

    private static final String fragmentShaderSource = "#version 330 core\n" + "out vec4 FragColor;\n\n"
            + "void main()\n" + "{\n" + "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" + "}\n";

    private int handle;

    PaddleShaderProgram() {
        final int vertexShaderName = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderName, vertexShaderSource);
        glCompileShader(vertexShaderName);

        int vertexShaderCompileStatus;
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer buffer = memAllocInt(1);
            glGetShaderiv(vertexShaderName, GL_COMPILE_STATUS, buffer);
            vertexShaderCompileStatus = buffer.get(0);
        }

        if (vertexShaderCompileStatus == 0) {
            System.out.println(glGetShaderInfoLog(vertexShaderName));
        }

        // Set up fragment shader
        final int fragmentShaderName = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderName, fragmentShaderSource);
        glCompileShader(fragmentShaderName);

        // Check fragment shader compilation
        int fragmentShaderCompileStatus;
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer buffer = memAllocInt(1);
            glGetShaderiv(fragmentShaderName, GL_COMPILE_STATUS, buffer);
            fragmentShaderCompileStatus = buffer.get(0);
        }

        if (fragmentShaderCompileStatus == 0) {
            System.out.println(glGetShaderInfoLog(fragmentShaderName));
        }

        // Create the program
        handle = glCreateProgram();
        glAttachShader(handle, vertexShaderName);
        glAttachShader(handle, fragmentShaderName);
        glLinkProgram(handle);

        glDeleteShader(vertexShaderName);
        glDeleteShader(fragmentShaderName);
    }

    int getHandle() {
        return handle;
    }
}
