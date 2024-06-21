package main.renderer;

import main.converter.Loader;
import main.entities.Camera;
import main.models.RawModel;
import main.models.TextureCubeModel;
import main.shaders.SkyBoxShader;
import main.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SkyBoxRenderer {
    private int size;
    private RawModel cube;
    private float[] vertices;
    private SkyBoxShader skyBoxShader;
    private TextureCubeModel skyDayTexture;
    private TextureCubeModel skyNightTexture;
    private float rotation;
    private float scalar;
    private float blendFactor;
    private float time;

    boolean goingDark;
    int dayLength;

    public SkyBoxRenderer(Matrix4f projection){
        this.size = 500;
        this.vertices = new float[]{
                -size,  size, -size,
                -size, -size, -size,
                size, -size, -size,
                size, -size, -size,
                size,  size, -size,
                -size,  size, -size,

                -size, -size,  size,
                -size, -size, -size,
                -size,  size, -size,
                -size,  size, -size,
                -size,  size,  size,
                -size, -size,  size,

                size, -size, -size,
                size, -size,  size,
                size,  size,  size,
                size,  size,  size,
                size,  size, -size,
                size, -size, -size,

                -size, -size,  size,
                -size,  size,  size,
                size,  size,  size,
                size,  size,  size,
                size, -size,  size,
                -size, -size,  size,

                -size,  size, -size,
                size,  size, -size,
                size,  size,  size,
                size,  size,  size,
                -size,  size,  size,
                -size,  size, -size,

                -size, -size, -size,
                -size, -size,  size,
                size, -size, -size,
                size, -size, -size,
                -size, -size,  size,
                size, -size,  size
        };

        String[] dayFilesPath = {skyRightImagePath, skyLeftImagePath, skyTopImagePath, skyBottomImagePath, skyBackImagePath, skyFrontImagePath};
        String[] nightFiesPath = {skyNightRightImagePath, skyNightLeftImagePath, skyNightTopImagePath, skyNightBottomImagePath, skyNightBackImagePath, skyNightFrontImagePath};

        this.cube = Loader.getInstance().loadToVao(vertices, 3);
        this.skyDayTexture = new TextureCubeModel(dayFilesPath, false);
        this.skyNightTexture = new TextureCubeModel(nightFiesPath, false);
        this.skyBoxShader = new SkyBoxShader(skyBoxVertexShaderPath, skyBoxFragmentShaderPath);

        this.scalar = 1/60f;
        this.blendFactor = 0.5f;
        this.dayLength = 10;

        skyBoxShader.start();
        skyBoxShader.uploadValue(projectionID, projection);
        skyBoxShader.uploadValue(fCubID, 0);
        skyBoxShader.uploadValue(sCubID, 1);
        skyBoxShader.stop();
    }

    public void render(Camera camera){
        skyBoxShader.start();
        Matrix4f view = Maths.createView(camera);
        view.m30(0);
        view.m31(0);
        view.m32(0);

        view.rotate((float) Math.toRadians(rotation += scalar), new Vector3f(0, 1, 0));
        skyBoxShader.uploadValue(viewID, view);
        skyBoxShader.uploadValue(fogColorID, clearColor);

        glBindVertexArray(cube.getVaoID());
        glEnableVertexAttribArray(0);
        bindTextures();

        glDrawArrays(GL_TRIANGLES, 0, cube.getVerticesCount());

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        skyBoxShader.stop();
    }

    public void bindTextures(){

        if(time > dayLength && goingDark){
            goingDark = false;
        }else if(time < 0){
            goingDark = true;
        }

        time += goingDark ? scalar : -scalar;
        blendFactor = time / dayLength;

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, skyDayTexture.getID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP, skyNightTexture.getID());
        skyBoxShader.uploadValue(blendFactorID, blendFactor);
    }
}
