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
        super.bindAttribute(0, positionID);
        super.bindAttribute(1, texCordsID);
        super.bindAttribute(2, normalsID);
    }

    public <T> void uploadValue(String name, T val){
        super.uploadValue(name, val);
    }

}
