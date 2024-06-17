package main;

import main.toolbox.Path;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Configuration {
    // Path
    public static String resPrefix = "res/";
    public static String texturePrefix = "res/textures/";
    public static String objectPrefix = "res/objects/";
    public static String shaderPrefix = "res/shaders/";

    // Colors
    public static Vector4f skyBlue = new Vector4f(0.5f, 0.5f, 1f, 1);
    public static Vector4f grey = new Vector4f(0.2f, 0.2f, 0.2f, 1);
    public static Vector4f fog = new Vector4f(0.7f, 0.7f, 0.7f, 1);

    // Window Settings
    public static final String WINDOW_TITLE = "Hosta";
    public static final int WINDOW_WIDTH = 1920;//1140;
    public static final int WINDOW_HEIGHT = 1020;//810;

    public static int shiftXAxis = 0;//1750;
    public static int shiftYAxis = 0;//-50;

    // Shaders
    public static String entityFragmentShaderPath = Path.fromShader("EntityFragmentShader");
    public static String entityVertexShaderPath = Path.fromShader("EntityVertexShader");
    public static String terrainFragmentShaderPath = Path.fromShader("terrainFragmentShader");
    public static String terrainVertexShaderPath = Path.fromShader("terrainVertexShader");

    // Background
    public static final Vector4f clearColor = skyBlue;

    // resources
    public static final String redAlphaImagePath = Path.fromRes("redAlpha.png");

    // textures
    public static final String terrainGrassImagePath = Path.fromTexture("terrainGrass.png");
    public static final String treeImagePath = Path.fromTexture("tree.png");
    public static final String grassImagePath = Path.fromTexture("grassTexture.png");
    public static final String lowPolyTreeImagePath = Path.fromTexture("lowPolyTree.png");
    public static final String fernImagePath = Path.fromTexture("fernAtlas.png");
    public static final String heightMapPath = Path.fromTexture("heightMap.png");
    public static final String personImagePath = Path.fromTexture("person.png");

    public static final String grassSecondTerrainImagePath = Path.fromTexture("terrainGrassSecond.png");
    public static final String mudTerrainImagePath = Path.fromTexture("mud.png");
    public static final String grassFlowerTerrainImagePath = Path.fromTexture("grassFlowers.png");
    public static final String pathTerrainImagePath = Path.fromTexture("path.png");
    public static final String blendMapImagePath = Path.fromTexture("blendMap.png");

    // Objects
    public static final String dragonObjPath = Path.fromObject("dragon");
    public static final String bunnyObjPath = Path.fromObject("bunny");
    public static final String fernObj = Path.fromObject("fern");
    public static final String treeObjPath =Path.fromObject("tree");
    public static final String grassObjPath =Path.fromObject("grass");
    public static final String lowPolyTreeObjPath = Path.fromObject("lowPolyTree");
    public static final String droneObjPath = Path.fromObject("drone");
    public static final String personObjPath = Path.fromObject("person");

    // Projection
    public static float FOV = 70;
    public static float NEAR_PLAN = 0.1f;
    public static float FAR_PLAN = 1000;

    // Camera
    public static float defaultPitch = 10;
    public static float defaultDistanceFromPlayer = 50;

    // Terrain
    public static int terrainYVal = 0;

    // Light
    public static Vector3f lightColor = (new Vector3f(1, 1, 1));
    public static Vector3f lightPosition = (new Vector3f(800, 50, 0));

    public static float defaultReflectivity = 1;
    public static float defaultShineDamper = 10;

    // Generate Settings
    public static int xMin = -500;
    public static int xMax = 500;

    public static int zMin = -600;
    public static int zMax = 0;

    public static float treeScale = 3;
    public static float fernScale = 0.5f;
    public static float lowPolyTreeScale = 0.45f;

    // shader
    public static String positionID = "vPos";
    public static String normalsID = "vNormals";
    public static String texCordsID = "vTexCords";

    public static String projectionID = "projection";
    public static String viewID = "view";
    public static String transformationID = "transformation";

    public static String lightPositionID = "lightPos";
    public static String lightColorID = "lightColor";

    public static String shineDamperID = "shineDamper";
    public static String reflectivityID = "reflectivity";

    public static String fakeLightningID = "fakeLightning";

    public static String skyColorID = "skyColor";

    public static String backgroundTextureID = "backgroundTexture";
    public static String rTextureID = "rTexture";
    public static String gTextureID = "gTexture";
    public static String bTextureID = "bTexture";
    public static String blendTextureID = "blendMap";

    public static String numberOfRowsID = "numberOfRows";
    public static String textureOffset = "texOffset";

    // PlayerController
    public static float runSpeed = 200;
    public static float turnAngle = 160;
    public static float gravity = -50;
    public static float jumpPower = 30;
}
