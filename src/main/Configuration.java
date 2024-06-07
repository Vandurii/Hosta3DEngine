package main;

import org.joml.Vector4f;

public class Configuration {
    public static final String WINDOW_TITLE = "Hosta";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int FPS_CAP = 120;

    public static String defaultFragmentShaderPath = "src/main/shaders/FragmentShader.glsl";
    public static String defaultVertexShaderPath = "src/main/shaders/VertexShader.glsl";

    public static final Vector4f clearColor = new Vector4f(0.2f, 0.2f, 0.2f, 1);

    public static final String minecraftImagePath = "res/minecraft.png";
    public static final String redAlphaImagePath = "res/redAlpha.png";

    public static final String dragonObjPath = "res/dragon.obj";

    public static float FOV = 70;
    public static float NEAR_PLAN = 0.1f;
    public static float FAR_PLAN = 1000;

}
