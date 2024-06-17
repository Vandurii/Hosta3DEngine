package main.renderer;


import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.ObjectModel;
import main.models.RawModel;
import main.models.TextureModel;
import main.shaders.EntityShader;
import main.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer{
    private EntityShader entityShader;

    public EntityRenderer(EntityShader entityShader, Matrix4f projection){
        this.entityShader = entityShader;
        this.entityShader.start();
        this.entityShader.uploadValue(projectionID, projection);
        this.entityShader.stop();
    }

    public void render(Map<ObjectModel, List<Entity>> entitiesList, Camera camera, Light light){
        entityShader.start();
        // Upload values to shader
        entityShader.uploadValue(skyColorID, clearColor);
        entityShader.uploadValue(lightColorID, light.getColor());
        entityShader.uploadValue(lightPositionID, light.getPosition());
        entityShader.uploadValue(viewID, Maths.createView(camera));

        for(Map.Entry<ObjectModel, List<Entity>> entry: entitiesList.entrySet()) {
            ObjectModel objectModel = entry.getKey();
            prepareObjectModel(objectModel);

            for(Entity entity: entry.getValue()) {
                RawModel rawModel = entity.getObjectModel().getRawModel();

                System.out.println(String.format("x:%.2f y:%.2f", entity.getTextureOffset().x, entity.getTextureOffset().y));
                entityShader.uploadValue(textureOffset, new Vector2f(entity.getTextureOffset()));
                entityShader.uploadValue(transformationID, Maths.transform(entity.getPosition(), entity.getRotation(), entity.getScale()));
                glDrawElements(GL_TRIANGLES, rawModel.getVerticesCount(), GL_UNSIGNED_INT, 0);
            }

            unbind(objectModel);
        }
        entityShader.stop();
    }

    public void prepareObjectModel(ObjectModel objectModel){
        RawModel rawModel = objectModel.getRawModel();
        TextureModel textureModel = objectModel.getTextureModel();

        if(textureModel.hasTransparency()){
            MasterRenderer.getInstance().disableCulling();
        }

        // upload shader values
        entityShader.uploadValue(numberOfRowsID, textureModel.getNumberOfRows());
        entityShader.uploadValue(fakeLightningID, textureModel.hasFakeLightning());
        entityShader.uploadValue(shineDamperID, textureModel.getShineDamper());
        entityShader.uploadValue(reflectivityID, textureModel.getReflectivity());

        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureModel.getID());
    }

    public void unbind(ObjectModel objectModel){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        MasterRenderer.getInstance().enableCulling();
    }
}


