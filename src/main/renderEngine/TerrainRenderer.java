package main.renderEngine;

import main.models.RawModel;
import main.shaders.TerrainShader;
import main.terrains.Terrain;
import main.textures.TextureModel;
import main.tollbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static main.Configuration.terrainZVal;
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
        terrainShader.uploadValue("projectionMatrix", projectionMatrix);
        terrainShader.stop();
    }

    public void render(List<Terrain> terrainList){
        for(Terrain terrain: terrainList){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            glDrawElements(GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }

    public void prepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        TextureModel textureModel = terrain.getTextureModel();
        terrainShader.uploadValue("shineDamper", textureModel.getShineDamper());
        terrainShader.uploadValue("reflectivity", textureModel.getReflectivity());

        glActiveTexture(0);
        glBindTexture(GL_TEXTURE_2D, textureModel.getTexID());
    }

    public void unbindTextureModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void loadModelMatrix(Terrain terrain) {
        Matrix4f transMatrix = Maths.createTransformMatrix(new Vector3f(terrain.getX(), terrainZVal, terrain.getZ()), new Vector3f(0), 1);
        terrainShader.uploadValue("transMatrix", transMatrix);
    }
}
