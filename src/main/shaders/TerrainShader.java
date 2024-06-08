package main.shaders;

import static main.Configuration.*;

public class TerrainShader extends ShaderProgram{
    private static String vertexFile = terrainVertexShaderPath;
    private static String fragmentFile = terrainFragmentShaderPath;

    public TerrainShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "vTexCords");
        super.bindAttribute(2, "vNormals");
    }

    public <T> void uploadValue(String name, T val){
        super.uploadValue(name, val);
    }

}
