package main.engineTester;

import main.controller.KeyController;
import main.entities.Camera;
import main.entities.Entity;
import main.models.TextureRawModel;
import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.models.RawModel;
import main.renderEngine.ObjLoader;
import main.renderEngine.Renderer;
import main.shaders.StaticShader;
import main.textures.TextureModel;
import main.tollbox.Maths;
import org.joml.Vector3f;

import static main.Configuration.dragonObjPath;
import static main.Configuration.minecraftImagePath;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGameLoop {


    public static void main(String[] args) {
        DisplayManager.createDisplay();

        KeyController keyController = new KeyController();

        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);
        Loader loader = new Loader();

        RawModel rawModel = new ObjLoader().loadObjModel(dragonObjPath, loader);
        TextureModel modelTexture = new TextureModel(loader.loadTexture(minecraftImagePath));
        TextureRawModel textureRawModel = new TextureRawModel(rawModel, modelTexture);

        Entity entity = new Entity(textureRawModel, new Vector3f(0, -3,-13), new Vector3f(0), 1);
        Camera camera = new Camera();

        while (!glfwWindowShouldClose(DisplayManager.getGLFW())) {
            keyController.update();

            entity.increaseRotation(new Vector3f(0, 1, 0f));
            renderer.prepare();
            shader.start();
            shader.uploadValue("viewMatrix", Maths.createViewMatrix(camera));
            renderer.render(entity, shader);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
