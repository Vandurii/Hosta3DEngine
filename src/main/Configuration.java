package main;

import org.joml.Vector4f;

public class Configuration {
    public static final String WINDOW_TITLE = "Hosta";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    public static String defaultFragmentShaderPath = "src/main/shaders/defaultFragmentShader.glsl";
    public static String defaultVertexShaderPath = "src/main/shaders/defaultVertexShader.glsl";
    public static String terrainFragmentShaderPath = "src/main/shaders/terrainFragmentShader.glsl";
    public static String terrainVertexShaderPath = "src/main/shaders/terrainVertexShader.glsl";

    public static final Vector4f clearColor = new Vector4f(0.5f, 0.5f, 1f, 1);


    public static final String redAlphaImagePath = "res/redAlpha.png";
    public static final String grassImagePath = "res/grass.png";
    public static final String treeImagePath = "res/tree.png";
    public static final String lowPolyTreeImagePath = "res/lowPolyTree.png";
    public static final String fernImagePath = "res/fern.png";

    // Obj
    public static final String dragonObjPath = "res/dragon.obj";
    public static final String fernObj = "res/fern.obj";
    public static final String treeObjPath = "res/tree.obj";
    public static final String lowPolyTreeObjPath = "res/lowPolyTree.obj";

    public static float FOV = 70;
    public static float NEAR_PLAN = 0.1f;
    public static float FAR_PLAN = 1000;

    public static int terrainZVal = -1;

}
