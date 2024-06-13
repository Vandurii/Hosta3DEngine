package main;

import main.controllers.KeyControls;

import main.controllers.MouseControls;
import main.converter.Loader;
import main.converter.ModelData;
import main.converter.ObjFileLoader;
import main.converter.ObjectLoader;
import main.entities.*;
import main.models.ObjectModel;
import main.models.RawModel;
import main.models.TextureModel;
import main.renderer.MasterRenderer;
import main.renderer.Window;
import main.terrains.Terrain;
import main.texture.TerrainTexture;
import main.texture.TerrainTexturePack;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static main.Configuration.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainLoop {
    private static Window window;
    private static Loader loader;
    private static MasterRenderer masterRenderer;

    private static double lastFrameTime;
    private static Random random = new Random();

    private static List<Entity> entities = new ArrayList<>();
    private static List<Terrain> terrainList = new ArrayList<>();

    private static Terrain terrain;

    public static void main(String[] args){
        // Init Scene.
        init();

        Light light = new Light(lightPosition, lightColor);

        // Grass stuff
        ModelData grassData = ObjectLoader.loadOBJ(grassObjPath);
        RawModel grassRawModel = loader.loadToVao(grassData.getVertices(), grassData.getIndices(), grassData.getTextureCoords(), grassData.getNormals());
        new ObjFileLoader().loadObjModel(dragonObjPath);
        TextureModel grassTextureModel = new TextureModel(grassImagePath, false, 0, 0, true, true);
        ObjectModel grassObjectModel = new ObjectModel(grassRawModel, grassTextureModel);

        // Dragon stuff
        ModelData data = ObjectLoader.loadOBJ(dragonObjPath);
        RawModel dragonRawModel = loader.loadToVao(data.getVertices(), data.getIndices(), data.getTextureCoords(), data.getNormals());
        TextureModel drgaonTextureModel = new TextureModel(redAlphaImagePath, false);
        ObjectModel dragonObjectModel = new ObjectModel(dragonRawModel, drgaonTextureModel);

        // Bunny stuff
        ModelData bunnyData = ObjectLoader.loadOBJ(bunnyObjPath);
        RawModel dragonRaw = loader.loadToVao(bunnyData.getVertices(), bunnyData.getIndices(), bunnyData.getTextureCoords(), bunnyData.getNormals());
        TextureModel bunnyTexture = new TextureModel(redAlphaImagePath, false);
        ObjectModel bunnyModel = new ObjectModel(dragonRaw, bunnyTexture);

        // Fern stuff
        RawModel fernRawModel = new ObjFileLoader().loadObjModel(fernObj);
        TextureModel fernTextureModel = new TextureModel(fernImagePath, true, 0, 0, true, true);
        ObjectModel fernObjectModel = new ObjectModel(fernRawModel, fernTextureModel);

        // Tree stuff
        RawModel treeRawModel = new ObjFileLoader().loadObjModel(treeObjPath);
        TextureModel treeTextureModel = new TextureModel(treeImagePath, true, 0, 0);
        ObjectModel treeObjectModel = new ObjectModel(treeRawModel, treeTextureModel);

        // LowPolyTree stuff
        RawModel lowPoyTreeRawModel = new ObjFileLoader().loadObjModel(lowPolyTreeObjPath);
        TextureModel lowPolyTreeTexture = new TextureModel(lowPolyTreeImagePath, true, 10, 0);
        ObjectModel lowPolyTreeObject = new ObjectModel(lowPoyTreeRawModel, lowPolyTreeTexture);

        // Terrain
        TerrainTexture backgroundTex = new TerrainTexture(new TextureModel(grassSecondTerrainImagePath, false, 20, 5).getID());
        TerrainTexture rTex = new TerrainTexture(new TextureModel(mudTerrainImagePath, false, 20, 5).getID());
        TerrainTexture gTex = new TerrainTexture(new TextureModel(grassFlowerTerrainImagePath, false, 20, 5).getID());
        TerrainTexture bTex = new TerrainTexture(new TextureModel(pathTerrainImagePath, false, 20, 5).getID());

        TerrainTexturePack terrainPack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
        TerrainTexture blendTexture = new TerrainTexture(new TextureModel(blendMapImagePath, false).getID());

         terrain = new Terrain(0, -1, terrainPack, blendTexture, heightMapPath);
       // Terrain terrain2 = new Terrain(-1, -1, terrainPack, blendTexture);
        terrainList.add(terrain);
      //  terrainList.add(terrain2);

        // GenerateEntities
        int defaultCount = 400;
        generateEntities(treeObjectModel, treeScale, defaultCount, terrain);
        generateEntities(lowPolyTreeObject, lowPolyTreeScale, defaultCount, terrain);
        generateEntities(fernObjectModel, fernScale, defaultCount, terrain);
        generateEntities(grassObjectModel, 1, defaultCount, terrain);

        Player player = new Player(bunnyModel, new Vector3f(0, 0, -10), new Vector3f(), 0.5f);
        Entity dragon = new Entity(dragonObjectModel, new Vector3f(15, 1, -100), new Vector3f(), 1);
        entities.add(dragon);
        entities.add(player);

        // camera
        Camera camera = new Camera(player);

        // init controller stuff
        EngineObject controller = new EngineObject("controller");
        controller.addComponent(new MouseControls());
        controller.addComponent(new KeyControls(player));

        while ( !glfwWindowShouldClose(window.getWindowPtr()) ) {
            // Calculate delta time.
            double time = glfwGetTime();
            float deltaTime = (float)(time - lastFrameTime);

            controller.update(deltaTime);
            player.update(deltaTime, terrain);
            camera.update(deltaTime);

            // Add Entities.
            for(Entity e: entities){
                masterRenderer.processEntity(e);
            }

            // Add terrains.
            for(Terrain Terrain : terrainList){
                masterRenderer.processTerrain(Terrain);
            }

            masterRenderer.render(camera, light);
            window.update();

            // Update last frame time.
            lastFrameTime = time;
        }

        // Destroy scene
        destroy();
    }

    public static void init(){
        window = Window.getInstance();
        window.create();
        masterRenderer = MasterRenderer.getInstance();
        loader = Loader.getInstance();
    }

    public static void generateEntities(ObjectModel objectModel, float scale, int count, Terrain terrain){
        for(int i = 0; i < count; i++) {

            int startX = (int) (terrain.getX());
            int startZ = (int) (terrain.getZ());

            int x = random.nextInt(startX, startX + terrain.getSize());
            int z = random.nextInt(startZ, startZ + terrain.getSize());
            float y = terrain.getHeightOfTerrain(x, z);

            entities.add(new Entity(objectModel, new Vector3f(x, y, z ), new Vector3f(), scale));
        }
    }

    public static void destroy(){
        loader.destroy();
        window.destroy();
    }
}
