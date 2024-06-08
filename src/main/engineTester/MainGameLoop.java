package main.engineTester;

import main.controller.KeyController;
import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.TextureRawModel;
import main.renderEngine.*;
import main.models.RawModel;
import main.terrains.Terrain;
import main.textures.TextureModel;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static main.Configuration.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGameLoop {


    public static void main(String[] args) {
        DisplayManager.createDisplay();
        KeyController keyController = new KeyController();
        MasterRenderer renderer = new MasterRenderer();

        Loader loader = new Loader();

        List<Entity> entities = new ArrayList<>();


        // Scene stuff
        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        // Dragon stuff
        RawModel dragonRawModel = new ObjLoader().loadObjModel(dragonObjPath, loader);

        TextureModel drgaonTextureModel = loader.loadTexture(redAlphaImagePath);
        drgaonTextureModel.setReflectivity(1);
        drgaonTextureModel.setShineDamper(10);

        TextureRawModel dragonTextureRawModel = new TextureRawModel(dragonRawModel, drgaonTextureModel);
        entities.add(new Entity(dragonTextureRawModel, new Vector3f(15, 1, -100), new Vector3f(), 1));

        // Grass stuff
        TextureModel grassTexture = loader.loadTexture(grassImagePath);
        grassTexture.setShineDamper(10);
        grassTexture.setReflectivity(1);

        List<Terrain> terrains = new ArrayList<>();
        terrains.add(new Terrain(0, -1, loader, grassTexture));
        terrains.add(new Terrain(-1, -1, loader, grassTexture));

        // tree stuff
        RawModel fernRawModel = new ObjLoader().loadObjModel(fernObj, loader);

        TextureModel fernTextureModel = loader.loadTexture(fernImagePath);
        drgaonTextureModel.setReflectivity(1);
        drgaonTextureModel.setShineDamper(10);

        TextureRawModel fernTextureRawModel = new TextureRawModel(fernRawModel, fernTextureModel);
        entities.add(new Entity(fernTextureRawModel, new Vector3f(0, terrainZVal, -100), new Vector3f(), 1));



        while (!glfwWindowShouldClose(DisplayManager.getGLFW())) {
            keyController.update();
            entities.get(0).increaseRotation(new Vector3f(0, 1, 0));

            // Add Entities.
            for(Entity e: entities){
                renderer.processEntity(e);
            }

            // Add terrains.
            for(Terrain terrain: terrains){
                renderer.addTerrain(terrain);
            }

            // Render scene.
            renderer.render(light, camera);

            // Update scene.
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
