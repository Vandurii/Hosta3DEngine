package main.renderEngine;

import main.models.RawModel;
import main.shaders.TerrainShader;
import main.terrains.Terrain;
import main.models.TextureModel;
import main.textures.TerrainTexturePack;
import main.tollbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {
    private TerrainShader terrainShader;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix){
        this.terrainShader = terrainShader;
        terrainShader.start();
        terrainShader.uploadValue(projectionID, projectionMatrix);

        terrainShader.uploadValue(backgroundTextureID, 0);
        terrainShader.uploadValue(rTextureID, 1);
        terrainShader.uploadValue(gTextureID, 2);
        terrainShader.uploadValue(bTextureID, 3);
        terrainShader.uploadValue(blendTextureID, 4);

        terrainShader.stop();
    }

    public void render(List<Terrain> terrainList){
        for(Terrain Terrain : terrainList){
            prepareTerrain(Terrain);
            loadModelMatrix(Terrain);
            glDrawElements(GL_TRIANGLES, Terrain.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }

    public void prepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        terrainShader.uploadValue(shineDamperID, 1);
        terrainShader.uploadValue(reflectivityID, 0);

        bindTextures(terrain);
    }

    public void bindTextures(Terrain terrain){
        TerrainTexturePack terrainTexturePack = terrain.getTerrainTexturePack();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBackgroundTexture().getTextureID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getRTexture().getTextureID());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getGTexture().getTextureID());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBTexture().getTextureID());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }


    public void unbindTextureModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void loadModelMatrix(Terrain Terrain) {
        Matrix4f transMatrix = Maths.createTransformMatrix(new Vector3f(Terrain.getX(), terrainYVal, Terrain.getZ()), new Vector3f(0), 1);
        terrainShader.uploadValue(transformationID, transMatrix);
    }
}
