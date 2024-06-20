package main.shaders;

import static main.Configuration.positionID;

public class GuiShader extends MasterShader{

    public GuiShader(String vertexSource, String fragmentSource) {
        super(vertexSource, fragmentSource);
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, positionID);
    }
}
