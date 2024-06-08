package main.shaders;

import static main.Configuration.defaultFragmentShaderPath;
import static main.Configuration.defaultVertexShaderPath;

public class StaticShader extends ShaderProgram{
    private static String vertexFile = defaultVertexShaderPath;
    private static String fragmentFile = defaultFragmentShaderPath;

    public StaticShader() {
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
