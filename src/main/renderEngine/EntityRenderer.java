package main.renderEngine;

import main.entities.Entity;
import main.models.RawModel;
import main.models.ObjectModel;
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

    public void render(Map<ObjectModel, List<Entity>> entitiesMap){
        for(ObjectModel objectModel : entitiesMap.keySet()){
            prepareTextureModel(objectModel);
            List<Entity> entityList = entitiesMap.get(objectModel);

            for(Entity entity: entityList){
                prepareInstance(entity);
                glDrawElements(GL_TRIANGLES, entity.getTExtureRawModel().getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    public void prepareTextureModel(ObjectModel objectModel){
        RawModel rawModel = objectModel.getRawModel();
        TextureModel textureModel = objectModel.getModelTexture();

        if(textureModel.hasTransparency()){
            MasterRenderer.disableCulling();
        }

        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.uploadValue("hasFakeLightning", textureModel.hasFakeLightning());
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

        MasterRenderer.enableCulling();
    }

    public void prepareInstance(Entity entity) {
        Matrix4f transMatrix = Maths.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.uploadValue("transMatrix", transMatrix);
    }
}
