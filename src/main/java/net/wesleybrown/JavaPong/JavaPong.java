package net.wesleybrown.JavaPong;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

final class JavaPong {

    /**
     * The window that will contain the OpenGL context to render to.
     */
    private long window;

    private Paddle playerPaddle;

    private int shaderProgram;
    private int vao;

    private JavaPong() {
        // GLFW has to be initialized
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        // Set up window hints
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);    // Required for macOS

        // LWJGL treats GLFWwindow* as long
        window = glfwCreateWindow(800, 600, "Hello, World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        GL.createCapabilities();    // Necessary for LWJGL to work correctly

        // Set up game
        playerPaddle = new Paddle(100, 100);

        initGl();
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void run() {
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(shaderProgram);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    private void initGl() {
        // Set up vertex shader
        final String vertexShaderSource = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n\n"
                + "void main()\n" + "{\n" + "    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" + "}\n";
        final int vertexShaderName = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderName, vertexShaderSource);
        glCompileShader(vertexShaderName);

        // Check vertex shader compilation
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
        final String fragmentShaderSource = "#version 330 core\n" + "out vec4 FragColor;\n\n" + "void main()\n"
                + "{\n" + "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" + "}\n";
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

        // Set up shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShaderName);
        glAttachShader(shaderProgram, fragmentShaderName);
        glLinkProgram(shaderProgram);
        // Shaders no longer needed after program is compiled
        glDeleteShader(vertexShaderName);
        glDeleteShader(fragmentShaderName);

        // Check shader program
        System.out.println(glGetProgramInfoLog(shaderProgram));

        // Set up VAO
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        final float[] vertices = {
                // left triangle
                -0.0625f,  0.125f,  0.0f,  // top-left
                -0.0625f, -0.125f,  0.0f,  // bottom-left
                 0.0625f,  0.125f,  0.0f,  // top-right

                // right triangle
                -0.0625f, -0.125f,  0.0f,  // bottom-left
                 0.0625f, -0.125f,  0.0f,  // bottom-right
                 0.0625f,  0.125f,  0.0f   // top-right
        };

        final int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = memAllocFloat(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }

        // Set up data pointers
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);
        glEnableVertexAttribArray(0);

        glBindVertexArray(vao); // unbind VAO
    }

    public static void main(String[] args) {
        final JavaPong javaPong = new JavaPong();
        javaPong.run();
    }
}
