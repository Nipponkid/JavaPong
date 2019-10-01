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
 * The shader program that all GameObjects use.
 */
final class ShaderProgram {

    private static final String vertexShaderSource =
              "#version 330 core\n\n"

            + "layout (location = 0) in vec3 aPos;\n"
            + "layout(location = 1) in vec4 vColor;\n\n"

            + "out vec4 color;\n\n"

            + "uniform mat4 transform;\n\n"

            + "void main()\n" + "{\n"
            + "    color = vColor;\n"
            + "    gl_Position = transform * vec4(aPos, 1.0f);\n"
            + "}\n";

    private static final String fragmentShaderSource =
              "#version 330 core\n\n"

            + "in vec4 color;\n\n"

            + "out vec4 FragColor;\n\n"

            + "void main()\n" + "{\n"
            + "    FragColor = color;\n"
            + "}\n";

    private int handle;

    ShaderProgram() {
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
