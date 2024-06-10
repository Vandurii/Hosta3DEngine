package main.engineTester;

import main.controller.KeyController;
import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.ObjectModel;
import main.renderEngine.*;
import main.models.RawModel;
import main.terrains.grassTerrain;
import main.models.TextureModel;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static main.Configuration.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGameLoop {
    private static Random random = new Random();
    private static List<Entity> entities = new ArrayList<>();
    private static List<grassTerrain> grassTerrains = new ArrayList<>();

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        KeyController keyController = new KeyController();
        MasterRenderer renderer = new MasterRenderer();
        Loader loader = new Loader();
        ObjLoader objLoader = new ObjLoader();

        // Scene stuff
        Camera camera = new Camera();
        Light light = new Light(lightPosition, lightColor);

        // Grass stuff
        ModelData grassData = OBJFileLoader.loadOBJ(grassObjPath);
        RawModel grassRawModel = loader.loadToVao(grassData.getVertices(), grassData.getIndices(), grassData.getTextureCoords(), grassData.getNormals());new ObjLoader().loadObjModel(dragonObjPath, loader);
        TextureModel grassTextureModel = loader.loadTexture(grassImagePath, false, 0, 0, true, true);
        ObjectModel grassObjectModel = new ObjectModel(grassRawModel, grassTextureModel);

        // Dragon stuff
        ModelData data = OBJFileLoader.loadOBJ(dragonObjPath);
        RawModel dragonRawModel = loader.loadToVao(data.getVertices(), data.getIndices(), data.getTextureCoords(), data.getNormals());new ObjLoader().loadObjModel(dragonObjPath, loader);
        TextureModel drgaonTextureModel = loader.loadTexture(redAlphaImagePath, false);
        ObjectModel dragonObjectModel = new ObjectModel(dragonRawModel, drgaonTextureModel);

        // Terrain stuff
        TextureModel grassTexture = loader.loadTexture(terrainGrassImagePath, true, 0, 0);

        // Fern stuff
        RawModel fernRawModel = new ObjLoader().loadObjModel(fernObj, loader);
        TextureModel fernTextureModel = loader.loadTexture(fernImagePath, true, 0, 0, true, true);
        ObjectModel fernObjectModel = new ObjectModel(fernRawModel, fernTextureModel);

        // Tree stuff
        RawModel treeRawModel = new ObjLoader().loadObjModel(treeObjPath, loader);
        TextureModel treeTextureModel = loader.loadTexture(treeImagePath, true, 0, 0);
        ObjectModel treeObjectModel = new ObjectModel(treeRawModel, treeTextureModel);

        // LowPolyTree stuff
        RawModel lowPoyTreeRawModel = new ObjLoader().loadObjModel(lowPolyTreeObjPath, loader);
        TextureModel lowPolyTreeTexture = loader.loadTexture(lowPolyTreeImagePath, true, 0, 0);
        ObjectModel lowPolyTreeObject = new ObjectModel(lowPoyTreeRawModel, lowPolyTreeTexture);

        // GenerateEntities
        int defaultCount = 400;
        generateEntities(treeObjectModel, treeScale, defaultCount);
        generateEntities(fernObjectModel, fernScale, defaultCount);
        generateEntities(lowPolyTreeObject, lowPolyTreeScale, defaultCount);
        generateEntities(grassObjectModel, 1, defaultCount);

        entities.add(new Entity(dragonObjectModel, new Vector3f(15, 1, -100), new Vector3f(), 1));

        // Generate Terrains
        grassTerrains.add(new grassTerrain(0, -1, loader, grassTexture));
        grassTerrains.add(new grassTerrain(-1, -1, loader, grassTexture));

        while (!glfwWindowShouldClose(DisplayManager.getGLFW())) {
            keyController.update();
            entities.get(entities.size() - 1).increaseRotation(new Vector3f(0, 1, 0));

            // Add Entities.
            for(Entity e: entities){
                renderer.processEntity(e);
            }

            // Add terrains.
            for(grassTerrain grassTerrain : grassTerrains){
                renderer.addTerrain(grassTerrain);
            }

            // Render scene.
            renderer.render(light, camera);

            // Update scene.
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public static void generateEntities(ObjectModel objectModel, float scale, int count){
        for(int i = 0; i < count; i++) {
            int x = random.nextInt(xMin, xMax);
            int z = random.nextInt(zMin, zMax);

            entities.add(new Entity(objectModel, new Vector3f(x, terrainYVal, z ), new Vector3f(), scale));
        }
    }
}
