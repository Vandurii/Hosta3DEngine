package main.renderEngine;

import main.entities.Entity;
import main.models.RawModel;
import main.models.TextureRawModel;
import main.shaders.StaticShader;
import main.textures.TextureModel;
import main.tollbox.Maths;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer {
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.uploadValue("projectionMatrix", projectionMatrix);
        shader.stop();
    }

    public void render(Map<TextureRawModel, List<Entity>> entitiesMap){
        for(TextureRawModel textureRawModel: entitiesMap.keySet()){
            prepareTextureModel(textureRawModel);
            List<Entity> entityList = entitiesMap.get(textureRawModel);

            for(Entity entity: entityList){
                prepareInstance(entity);
                glDrawElements(GL_TRIANGLES, entity.getTExtureRawModel().getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    public void prepareTextureModel(TextureRawModel textureRawModel){
        RawModel rawModel = textureRawModel.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        TextureModel textureModel = textureRawModel.getModelTexture();
        shader.uploadValue("shineDamper", textureModel.getShineDamper());
        shader.uploadValue("reflectivity", textureModel.getReflectivity());

        glActiveTexture(0);
        glBindTexture(GL_TEXTURE_2D, textureModel.getTexID());
    }

    public void unbindTextureModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void prepareInstance(Entity entity) {
        Matrix4f transMatrix = Maths.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.uploadValue("transMatrix", transMatrix);
    }
}
