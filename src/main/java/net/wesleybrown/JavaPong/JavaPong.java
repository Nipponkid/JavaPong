package net.wesleybrown.JavaPong;

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

    private Paddle playerPaddle;
    private Paddle opponentPaddle;
    private GameObject ball;

    /**
     * Used to render both the player and opponent paddles.
     */
    private PaddleRenderer paddleRenderer;

    private GameObjectRenderer gameObjectRenderer;

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
        playerPaddle = Paddle.atPositionAtScale(new Vector3f(-0.25f, 0.0f, 0.0f), 1.0f);
        opponentPaddle = Paddle.atPositionAtScale(new Vector3f(0.25f, 0.0f, 0.0f), 1.0f);
        ball = new GameObject("Ball",
                Model.SQUARE, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0005f, 0.0005f, 1.0f));

        paddleRenderer = new PaddleRenderer();
        gameObjectRenderer = new GameObjectRenderer();
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
        playerPaddle.update(timesliceMS);
        ball.update(timesliceMS);
        if (detectCollisions()) {
            System.out.println("Collision Detected!");
        }
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

        paddleRenderer.render(playerPaddle);
        paddleRenderer.render(opponentPaddle);
        gameObjectRenderer.render(ball);
    }

    private boolean detectCollisions() {
        // If the x coordinate of the top-right point of the ball's collision box is >= the x coordinate of the
        // top-left point of a paddle's collision box AND the x coordinate of the top-right point of a paddle's
        // collision box is >= the x coordinate of the top-left point of the ball's collision box, then they are
        // overalpping along the x axis and are thus colliding along the x axis.
        boolean isXCollision = ball.getPosition().x() + (ball.getScale().x() / 2.0f) >= playerPaddle.getPosition().x()
                && playerPaddle.getPosition().x() + (playerPaddle.getWidth() / 2.0f) >= ball.getPosition().x();
        boolean isYCollision = ball.getPosition().y() + (ball.getScale().y() / 2.0f) >= playerPaddle.getPosition().y()
                && playerPaddle.getPosition().y() + (playerPaddle.getWidth() / 2.0f) >= ball.getPosition().y();
        return isXCollision && isYCollision;
    }

    public static void main(String[] args) {
        final JavaPong javaPong = new JavaPong();
        javaPong.run();
    }
}
