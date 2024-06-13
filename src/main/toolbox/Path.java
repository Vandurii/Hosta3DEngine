package main.toolbox;

import main.Configuration;

public class Path {
    private static String resPrefix = Configuration.resPrefix;
    private static String texturePrefix = Configuration.texturePrefix;
    private static String objectPrefix = Configuration.objectPrefix;
    private static String shaderPrefix = Configuration.shaderPrefix;

    private static String objSuffix = ".obj";
    private static String shadeSuffix = ".glsl";

    public static String fromTexture(String string){
        return texturePrefix + string;
    }

    public static String fromObject(String string){
        return objectPrefix + string + objSuffix;
    }

    public static String fromRes(String string){
        return resPrefix + string;
    }

    public static String fromShader(String string){
        return shaderPrefix + string + shadeSuffix;
    }
}
