package main.shaders;

import static main.Configuration.*;

public class EntityShader extends MasterShader{
    public EntityShader(String vertexSource, String fragmentSource) {
        super(vertexSource, fragmentSource);
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, positionID);
        bindAttribute(1, texCordsID);
        bindAttribute(2, normalsID);
    }
}
