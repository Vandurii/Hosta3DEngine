package main.shaders;

import static main.Configuration.positionID;

public class SkyBoxShader extends MasterShader{

    public SkyBoxShader(String vertexSource, String fragmentSource) {
        super(vertexSource, fragmentSource);
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, positionID);
    }
}
