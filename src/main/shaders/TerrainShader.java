package main.shaders;

import static main.Configuration.*;

public class TerrainShader extends MasterShader{
    public TerrainShader(String vertexSource, String fragmentSource) {
        super(vertexSource, fragmentSource);
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, positionID);
        bindAttribute(1, texCordsID);
        bindAttribute(2, normalsID);
    }
}
