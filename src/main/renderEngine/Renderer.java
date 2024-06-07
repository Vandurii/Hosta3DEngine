package main.renderEngine;

import main.entities.Entity;
import main.models.RawModel;
import main.models.TextureRawModel;
import main.shaders.StaticShader;
import main.tollbox.Maths;
import org.joml.Matrix4f;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {
    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader){
        shader.start();
        createProjectionMatrix();
        projectionMatrix = createProjectionMatrix();
        shader.uploadValue("projectionMatrix", projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearColor(clearColor.x, clearColor.y, clearColor.z,1);
    }

    public void render(Entity entity, StaticShader shader){
        TextureRawModel textureRawModel = entity.getTextureModel();
        RawModel rawModel = textureRawModel.getRawModel();

        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        Matrix4f transMatrix = Maths.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.uploadValue("transMatrix", transMatrix);

        glActiveTexture(0);
        glBindTexture(GL_TEXTURE_2D, textureRawModel.getModelTexture().getTexID());
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public Matrix4f createProjectionMatrix(){
        float aspectRatio = (float)WINDOW_WIDTH / (float)WINDOW_HEIGHT;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLAN - NEAR_PLAN;

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLAN + NEAR_PLAN) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLAN * FAR_PLAN) / frustumLength));
        projectionMatrix.m33(0);

        return projectionMatrix;
    }
}
