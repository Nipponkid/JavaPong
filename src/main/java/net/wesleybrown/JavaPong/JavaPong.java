package net.wesleybrown.JavaPong;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
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

    private PaddleRenderer paddleRenderer;

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
        playerPaddle = new Paddle(new Vector3f(-0.25f, 0.128f, 0.0f));
        paddleRenderer = new PaddleRenderer();

        initGl();
    }

    private void loop() {
        long last = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window)) {
            long now = System.currentTimeMillis();

            while (last < now) {
                last += 16;
                update(16);
            }

            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    /**
     * Update a game of Pong.
     * @param timesliceMS
     */
    private void update(final long timesliceMS) {
        final Vector3f velocity = new Vector3f(0.0f, 0.01f, 0.0f);
        playerPaddle.setPosition(playerPaddle.getPosition().add(velocity));
    }

    private void run() {
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    /**
     * Render a game of Pong.
     */
    private void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(shaderProgram);
        glBindVertexArray(vao);

        paddleRenderer.render(playerPaddle);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    private void initGl() {
        // Set up vertex shader
        final String vertexShaderSource = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n\n"
                + "uniform mat4 transform;\n" + "void main()\n" + "{\n"
                + "    gl_Position = transform * vec4(aPos, 1.0f);\n" + "}\n";
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

        // Set up uniforms
        final Matrix4f trans = new Matrix4f();

        glUseProgram(shaderProgram);    // glGetUniformLocation requires a shader program to be being used
        final int transformLocation = glGetUniformLocation(shaderProgram, "transform");
        System.out.println(transformLocation);
        try (final MemoryStack stack = stackPush()) {
            final FloatBuffer buffer = memAllocFloat(16);
            trans.get(buffer);  // Don't need to flip because JOML does for us
            System.out.println(buffer.get(3));
            glUniformMatrix4fv(transformLocation, false, buffer);
        }

        glBindVertexArray(vao); // unbind VAO
    }

    public static void main(String[] args) {
        final JavaPong javaPong = new JavaPong();
        javaPong.run();
    }
}
