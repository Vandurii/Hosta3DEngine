package main;

import main.controller.KeyController;
import main.controller.MouseListener;
import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.entities.Player;
import main.models.ObjectModel;
import main.renderEngine.*;
import main.models.RawModel;
import main.terrains.Terrain;
import main.models.TextureModel;
import main.textures.TerrainTexture;
import main.textures.TerrainTexturePack;
import org.joml.Vector3f;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static main.Configuration.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainLoop {
    private static Random random = new Random();
    private static List<Entity> entities = new ArrayList<>();
    private static List<Terrain> Terrains = new ArrayList<>();

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        KeyController keyController = new KeyController();
        MasterRenderer renderer = new MasterRenderer();
        Loader loader = new Loader();
        ObjLoader objLoader = new ObjLoader();
        float lastFrameTime = (float) glfwGetTime();

        // Terrain
        TerrainTexture backgroundTex = new TerrainTexture(loader.loadTexture(grassSecondTerrainImagePath, true).getTexID());
        TerrainTexture rTex = new TerrainTexture(loader.loadTexture(mudTerrainImagePath, true).getTexID());
        TerrainTexture gTex = new TerrainTexture(loader.loadTexture(grassFlowerTerrainImagePath, true).getTexID());
        TerrainTexture bTex = new TerrainTexture(loader.loadTexture(pathTerrainImagePath, true).getTexID());

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
        TerrainTexture blendTex = new TerrainTexture(loader.loadTexture(blendMapImagePath, false).getTexID());

        // Scene stuff
        Light light = new Light(lightPosition, lightColor);

        // Player
        ModelData bunnyData = OBJFileLoader.loadOBJ(bunnyObjPath);
        RawModel bunnyRawModel = loader.loadToVao(bunnyData.getVertices(), bunnyData.getIndices(), bunnyData.getTextureCoords(), bunnyData.getNormals());
        TextureModel bunnyTextureModel = loader.loadTexture(redAlphaImagePath, false, 100, 0, false, false);
        ObjectModel bunnyObjectModel = new ObjectModel(bunnyRawModel, bunnyTextureModel);

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
        TextureModel lowPolyTreeTexture = loader.loadTexture(lowPolyTreeImagePath, true, 10, 0);
        ObjectModel lowPolyTreeObject = new ObjectModel(lowPoyTreeRawModel, lowPolyTreeTexture);

        // GenerateEntities
        int defaultCount = 400;
        generateEntities(treeObjectModel, treeScale, defaultCount);
        generateEntities(fernObjectModel, fernScale, defaultCount);
        generateEntities(lowPolyTreeObject, lowPolyTreeScale, defaultCount);
        generateEntities(grassObjectModel, 1, defaultCount);

        Entity dragon = new Entity(dragonObjectModel, new Vector3f(15, 1, -100), new Vector3f(), 1);
        Player player = new Player(bunnyObjectModel, new Vector3f(0, 1, -10), new Vector3f(), 0.25f);
        entities.add(dragon);
        entities.add(player);

        // Generate Terrains
        Terrains.add(new Terrain(0, -1, loader, texturePack, blendTex));
        Terrains.add(new Terrain(-1, -1, loader, texturePack, blendTex));

        // Camera
        Camera camera = new Camera(player);

        while (!glfwWindowShouldClose(DisplayManager.getGLFW())) {
            light.setPosition(new Vector3f(player.getPosition().x,player.getPosition().z , player.getPosition().z));
            dragon.increaseRotation(new Vector3f(0, 1, 0));

            float time = (float) glfwGetTime();
            float deltaTime = time - lastFrameTime;

            keyController.update(player);
            player.move(deltaTime);
            camera.update(deltaTime);

            // Add Entities.
            for(Entity e: entities){
                renderer.processEntity(e);
            }

            // Add terrains.
            for(Terrain Terrain : Terrains){
                renderer.addTerrain(Terrain);
            }

            // Render scene.
            renderer.render(light, camera);

            // Update scene.
            DisplayManager.updateDisplay();

            lastFrameTime = time;

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
