package net.wesleybrown.javapong;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import static org.lwjgl.opengl.GL30.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_TRUE;
import static org.lwjgl.opengl.GL30.glClear;
import static org.lwjgl.opengl.GL30.glClearColor;

import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector3f;

import org.lwjgl.opengl.GL;

final class JavaPong {

    /**
     * The window that will contain the OpenGL context to render to.
     */
    private long window;

    private GameObject playerPaddle;
    private GameObject opponentPaddle;
    private GameObject ball;

    private GameObject topBoundary;
    private GameObject bottomBoundary;

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

        // Set up keyboard input detection
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_W && action == GLFW_PRESS) {
                final Vector3f velocity = new Vector3f(0.0f, 0.01f, 0.0f);
                playerPaddle.setVelocity(velocity);
            } else if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
                final Vector3f velocity = new Vector3f();   // zero vector
                playerPaddle.setVelocity(velocity);
            } else if (key == GLFW_KEY_S && action == GLFW_PRESS) {
                final Vector3f velocity = new Vector3f(0.0f, -0.01f, 0.0f);
                playerPaddle.setVelocity(velocity);
            } else if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
                final Vector3f velocity = new Vector3f();
                playerPaddle.setVelocity(velocity);
            }
        });

        // Set up game
        playerPaddle = new GameObject("Player Paddle", Model.SQUARE, new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.5f, 2.0f, 1.0f));
        opponentPaddle = new GameObject("Opponent Paddle", Model.SQUARE, new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.5f, 2.0f, 1.0f));
        ball = new GameObject("Ball", Model.SQUARE, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.25f, 0.25f, 1.0f));
        ball.setVelocity(new Vector3f(0.001f, 0.001f, 0.0f));

        // Boundaries
        topBoundary = new GameObject("Top Boundary", Model.SQUARE, new Vector3f(0.0f, 4.5f, 0.0f), new Vector3f(10.0f, 1.0f, 1.0f));
        bottomBoundary = new GameObject("Bottom Boundary", Model.SQUARE, new Vector3f(0.0f, -4.5f, 0.0f), new Vector3f(10.0f, 1.0f, 1.0f));
    }

    private void loop() {
        long last = System.currentTimeMillis();
        final long MS_PER_TICK = 16;
        long accumulatedMS = 0;

        while (!glfwWindowShouldClose(window)) {
            final long now = System.currentTimeMillis();
            final long elapsedMS = now - last;
            last = now;
            accumulatedMS += elapsedMS;

            glfwPollEvents();

            while (accumulatedMS >= MS_PER_TICK) {
                update(MS_PER_TICK);
                accumulatedMS -= MS_PER_TICK;
            }

            render((float) (accumulatedMS / MS_PER_TICK));
        }
    }

    /**
     * Update a game of Pong.
     * @param timesliceMS
     */
    private void update(final long timesliceMS) {
        ball.update(timesliceMS);
        playerPaddle.update(timesliceMS);
        opponentPaddle.update(timesliceMS);

        simulateCollisions(timesliceMS);
    }

    private void run() {
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void simulateCollisions(final long timesliceMS) {
        if (ball.isCollidingWith(playerPaddle)) {
            ball.respondToCollisionWith(playerPaddle);
        }

        if (ball.isCollidingWith(opponentPaddle)) {
            ball.respondToCollisionWith(opponentPaddle);
        }

        if (ball.isCollidingWith(topBoundary)) {
            ball.respondToCollisionWith(topBoundary);
        }

        if (ball.isCollidingWith(bottomBoundary)) {
            ball.respondToCollisionWith(bottomBoundary);
        }
    }

    /**
     * Render a game of Pong.
     */
    private void render(final float deltaTime) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        ball.render(deltaTime);
        playerPaddle.render(deltaTime);
        opponentPaddle.render(deltaTime);

        topBoundary.render(deltaTime);
        bottomBoundary.render(deltaTime);

        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        final JavaPong javaPong = new JavaPong();
        javaPong.run();
    }
}
