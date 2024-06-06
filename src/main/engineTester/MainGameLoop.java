package main.engineTester;

import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.RawModel;
import main.renderEngine.Renderer;
import main.shaders.ShaderProgram;
import main.shaders.StaticShader;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGameLoop {

    public static float[] vertices = {
            -0.5f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f,
             0.5f, -0.5f, 0f
    };

    public static int[] indices = {
            0, 1, 2, 2, 1, 3
    };

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer();
        Loader loader = new Loader();

        RawModel rawModel = loader.loadToVao(vertices, indices);

        while (!glfwWindowShouldClose(DisplayManager.getGLFW())) {
            renderer.prepare();
            shader.start();
            renderer.render(rawModel);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
