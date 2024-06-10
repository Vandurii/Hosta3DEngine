package main.renderEngine;

import main.models.RawModel;
import main.shaders.TerrainShader;
import main.terrains.grassTerrain;
import main.models.TextureModel;
import main.tollbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {
    private TerrainShader terrainShader;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix){
        this.terrainShader = terrainShader;
        terrainShader.start();
        terrainShader.uploadValue(projectionID, projectionMatrix);
        terrainShader.stop();
    }

    public void render(List<grassTerrain> grassTerrainList){
        for(grassTerrain grassTerrain : grassTerrainList){
            prepareTerrain(grassTerrain);
            loadModelMatrix(grassTerrain);
            glDrawElements(GL_TRIANGLES, grassTerrain.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }

    public void prepareTerrain(grassTerrain grassTerrain){
        RawModel rawModel = grassTerrain.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        TextureModel textureModel = grassTerrain.getTextureModel();
        terrainShader.uploadValue(shineDamperID, textureModel.getShineDamper());
        terrainShader.uploadValue(reflectivityID, textureModel.getReflectivity());

        glActiveTexture(0);
        glBindTexture(GL_TEXTURE_2D, textureModel.getTexID());
    }

    public void unbindTextureModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void loadModelMatrix(grassTerrain grassTerrain) {
        Matrix4f transMatrix = Maths.createTransformMatrix(new Vector3f(grassTerrain.getX(), terrainYVal, grassTerrain.getZ()), new Vector3f(0), 1);
        terrainShader.uploadValue(transformationID, transMatrix);
    }
}
