package main;

import main.tollbox.Path;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Configuration {
    // Path
    public static String resPrefix = "res/";
    public static String texturePrefix = "res/textures/";
    public static String objectPrefix = "res/objects/";
    public static String shaderPrefix = "src/main/shaders/";

    // Window Settings
    public static final String WINDOW_TITLE = "Hosta";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    // Shaders
    public static String defaultFragmentShaderPath = Path.fromShader("EntityFragmentShader");
    public static String defaultVertexShaderPath = Path.fromShader("EntityVertexShader");
    public static String terrainFragmentShaderPath = Path.fromShader("terrainFragmentShader");
    public static String terrainVertexShaderPath = Path.fromShader("terrainVertexShader");

    // Background
    public static final Vector4f clearColor = new Vector4f(0.5f, 0.5f, 1f, 1);

    // resources
    public static final String redAlphaImagePath = Path.fromRes("redAlpha.png");

    // textures
    public static final String grassImagePath = Path.fromTexture("terrainGrass.png");
    public static final String treeImagePath = Path.fromTexture("tree.png");
    public static final String lowPolyTreeImagePath = Path.fromTexture("lowPolyTree.png");
    public static final String fernImagePath = Path.fromTexture("fern.png");

    // Objects
    public static final String dragonObjPath = Path.fromObject("dragon");
    public static final String bunnyObjPath = Path.fromObject("bunny");
    public static final String fernObj = Path.fromObject("fern");
    public static final String treeObjPath =Path.fromObject("tree");
    public static final String grassObjPath =Path.fromObject("grass");
    public static final String lowPolyTreeObjPath = Path.fromObject("lowPolyTree");

    // Projection
    public static float FOV = 70;
    public static float NEAR_PLAN = 0.1f;
    public static float FAR_PLAN = 1000;

    // Terrain
    public static int terrainYVal = -3;

    // Light
    public static Vector3f lightColor = (new Vector3f(1, 1, 1));
    public static Vector3f lightPosition = (new Vector3f(100, 0, 0));

    public static float defaultReflectivity = 1;
    public static float defaultShineDamper = 10;

    // Generate Settings
    public static int xMin = -500;
    public static int xMax = 500;

    public static int zMin = -200;
    public static int zMax = 0;

    public static float treeScale = 3;
    public static float fernScale = 0.5f;
    public static float lowPolyTreeScale = 0.45f;

}
