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
    public static Vector4f cloudsColor = new Vector4f(0.54444f, 0.62f, 0.69f, 1);

    public static Vector4f grey = new Vector4f(0.2f, 0.2f, 0.2f, 1);
    public static Vector4f fog = new Vector4f(0.7f, 0.7f, 0.7f, 1);

    // Window Settings
    public static final String WINDOW_TITLE = "Hosta";
    public static final int WINDOW_WIDTH = 1520;//1140;
    public static final int WINDOW_HEIGHT = 1080;

    public static int shiftXAxis = 1750;
    public static int shiftYAxis = -50;

    // Shaders
    public static String entityFragmentShaderPath = Path.fromShader("EntityFragmentShader");
    public static String entityVertexShaderPath = Path.fromShader("EntityVertexShader");
    public static String terrainFragmentShaderPath = Path.fromShader("terrainFragmentShader");
    public static String terrainVertexShaderPath = Path.fromShader("terrainVertexShader");
    public static String guiFragmentShaderPath = Path.fromShader("GuiFragmentShader");
    public static String guiVertexShaderPath = Path.fromShader("GuiVertexShader");
    public static String skyBoxFragmentShaderPath = Path.fromShader("skyBoxFragmentShader");
    public static String skyBoxVertexShaderPath = Path.fromShader("skyBoxVertexShader");

    // Background
    public static final Vector4f clearColor = cloudsColor;

    // resources
    public static final String redAlphaImagePath = Path.fromRes("redAlpha.png");

    // textures
    public static final String terrainGrassImagePath = Path.fromTexture("terrainGrass.png");
    public static final String treeImagePath = Path.fromTexture("tree.png");
    public static final String grassImagePath = Path.fromTexture("flowerTexture.png");
    public static final String lowPolyTreeImagePath = Path.fromTexture("lowPolyTree.png");
    public static final String fernImagePath = Path.fromTexture("fernAtlas.png");
    public static final String heightMapPath = Path.fromTexture("heightMap.png");
    public static final String personImagePath = Path.fromTexture("person.png");
    public static final String lampImagePath = Path.fromTexture("lamp.png");

    public static final String grassSecondTerrainImagePath = Path.fromTexture("terrainGrassSecond.png");
    public static final String mudTerrainImagePath = Path.fromTexture("mud.png");
    public static final String grassFlowerTerrainImagePath = Path.fromTexture("grassFlowers.png");
    public static final String pathTerrainImagePath = Path.fromTexture("path.png");
    public static final String blendMapImagePath = Path.fromTexture("blendMap.png");

    public static final String skyBottomImagePath = Path.fromTexture("bottom.png");
    public static final String skyTopImagePath = Path.fromTexture("top.png");
    public static final String skyLeftImagePath = Path.fromTexture("left.png");
    public static final String skyRightImagePath = Path.fromTexture("right.png");
    public static final String skyFrontImagePath = Path.fromTexture("front.png");
    public static final String skyBackImagePath = Path.fromTexture("back.png");

    public static final String skyNightBottomImagePath = Path.fromTexture("nightBottom.png");
    public static final String skyNightTopImagePath = Path.fromTexture("nightTop.png");
    public static final String skyNightLeftImagePath = Path.fromTexture("nightLeft.png");
    public static final String skyNightRightImagePath = Path.fromTexture("nightRight.png");
    public static final String skyNightFrontImagePath = Path.fromTexture("nightFront.png");
    public static final String skyNightBackImagePath = Path.fromTexture("nightBack.png");

    public static final String haspidImagePath = Path.fromTexture("haspid.png");
    public static final String healthImagePath = Path.fromTexture("health.png");

    // Objects
    public static final String dragonObjPath = Path.fromObject("dragon");
    public static final String bunnyObjPath = Path.fromObject("bunny");
    public static final String fernObj = Path.fromObject("fern");
    public static final String treeObjPath =Path.fromObject("tree");
    public static final String grassObjPath =Path.fromObject("grass");
    public static final String lowPolyTreeObjPath = Path.fromObject("lowPolyTree");
    public static final String droneObjPath = Path.fromObject("drone");
    public static final String personObjPath = Path.fromObject("person");
    public static final String lampObjPath = Path.fromObject("lamp");

    // Projection
    public static float FOV = 70;
    public static float NEAR_PLAN = 0.1f;
    public static float FAR_PLAN = 1000;

    // Camera
    public static float defaultPitch = 10;
    public static float defaultDistanceFromPlayer = 100;

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

    public static String attenuationID = "attenuation";

    public static String fogColorID = "fogColor";
    public static String fCubID = "fCub";
    public static String sCubID = "sCub";
    public static String blendFactorID = "blendFactor";

    // PlayerController
    public static float runSpeed = 200;
    public static float turnAngle = 160;
    public static float gravity = -50;
    public static float jumpPower = 20;
}
