package main;

import main.controllers.KeyControls;

import main.controllers.MouseControls;
import main.converter.Loader;
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
        RawModel grassRawModel = ObjectLoader.loadOBJ(grassObjPath);
        TextureModel grassTextureModel =  new TextureModel.Builder(grassImagePath).transparent().fakeLighted().build();
        ObjectModel grassObjectModel = new ObjectModel(grassRawModel, grassTextureModel);

        // Dragon stuff
        RawModel dragonRawModel = ObjectLoader.loadOBJ(dragonObjPath);
        TextureModel drgaonTextureModel =  new TextureModel.Builder(redAlphaImagePath).build();
        ObjectModel dragonObjectModel = new ObjectModel(dragonRawModel, drgaonTextureModel);

        // Bunny stuff
        RawModel dragonRaw = ObjectLoader.loadOBJ(bunnyObjPath);
        TextureModel bunnyTexture =  new TextureModel.Builder(redAlphaImagePath).reflectivity(defaultReflectivity).shineDamper(defaultShineDamper).build();
        ObjectModel bunnyModel = new ObjectModel(dragonRaw, bunnyTexture);

        // Player
        RawModel playerRaw = ObjectLoader.loadOBJ(personObjPath);
        TextureModel playerTexture = new TextureModel.Builder(personImagePath).build();
        ObjectModel playerModel = new ObjectModel(playerRaw, playerTexture);

        // Fern stuff
        RawModel fernRawModel = ObjectLoader.loadOBJ(fernObj);
        TextureModel fernTextureModel =  new TextureModel.Builder(fernImagePath).fakeLighted().build();
        ObjectModel fernObjectModel = new ObjectModel(fernRawModel, fernTextureModel);

        // Tree stuff
        RawModel treeRawModel = ObjectLoader.loadOBJ(treeObjPath);
        TextureModel treeTextureModel =  new TextureModel.Builder(treeImagePath).build();;
        ObjectModel treeObjectModel = new ObjectModel(treeRawModel, treeTextureModel);

        // LowPolyTree stuff
        RawModel lowPoyTreeRawModel =ObjectLoader.loadOBJ(lowPolyTreeObjPath);
        TextureModel lowPolyTreeTexture = new TextureModel.Builder(lowPolyTreeImagePath).shineDamper(10).build();
        ObjectModel lowPolyTreeObject = new ObjectModel(lowPoyTreeRawModel, lowPolyTreeTexture);

        // Terrain
        TerrainTexture backgroundTex = new TerrainTexture(new TextureModel.Builder(grassSecondTerrainImagePath).build().getID());
        TerrainTexture rTex = new TerrainTexture(new TextureModel.Builder(mudTerrainImagePath).build().getID());
        TerrainTexture gTex = new TerrainTexture(new TextureModel.Builder(grassFlowerTerrainImagePath).build().getID());
        TerrainTexture bTex = new TerrainTexture(new TextureModel.Builder(pathTerrainImagePath).build().getID());

        TerrainTexturePack terrainPack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
        TerrainTexture blendTexture = new TerrainTexture(new TextureModel.Builder(blendMapImagePath).build().getID());

         terrain = new Terrain(0, 0, terrainPack, blendTexture, heightMapPath);
       // Terrain terrain2 = new Terrain(-1, -1, terrainPack, blendTexture);
        terrainList.add(terrain);
      //  terrainList.add(terrain2);

        // GenerateEntities
        int defaultCount = 400;
        generateEntities(treeObjectModel, treeScale, defaultCount, terrain);
        generateEntities(lowPolyTreeObject, lowPolyTreeScale, defaultCount, terrain);
        generateEntities(fernObjectModel, fernScale, defaultCount, terrain);
        generateEntities(grassObjectModel, 1, defaultCount, terrain);

        Player player = new Player(playerModel, new Vector3f(0, 0, 0), new Vector3f(), 0.5f);
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
