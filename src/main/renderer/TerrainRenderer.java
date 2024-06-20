package main.renderer;

import main.entities.Camera;
import main.entities.Light;
import main.models.RawModel;
import main.models.TextureModel;
import main.shaders.TerrainShader;
import main.terrains.Terrain;
import main.texture.TerrainTexturePack;
import main.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {
    private TerrainShader terrainShader;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projection){
        this.terrainShader = terrainShader;
        this.terrainShader.start();
        this.terrainShader.uploadValue(projectionID, projection);

        terrainShader.uploadValue(backgroundTextureID, 0);
        terrainShader.uploadValue(rTextureID, 1);
        terrainShader.uploadValue(gTextureID, 2);
        terrainShader.uploadValue(bTextureID, 3);
        terrainShader.uploadValue(blendTextureID, 4);

        this.terrainShader.stop();
    }

    public void render(List<Terrain> terrainList, Camera camera, Light light){
        terrainShader.start();

        // Upload values to shader
        terrainShader.uploadValue(skyColorID, clearColor);
        terrainShader.uploadValue(lightColorID, light.getColor());
        terrainShader.uploadValue(attenuationID, light.getAttenuation());
        terrainShader.uploadValue(lightPositionID, light.getPosition());
        terrainShader.uploadValue(viewID, Maths.createView(camera));

        for(Terrain terrain: terrainList) {
            RawModel rawModel = terrain.getRawModel();

            prepareTerrain(terrain);
            terrainShader.uploadValue(transformationID, Maths.transform(new Vector3f(terrain.getX(), terrainYVal, terrain.getZ()), new Vector3f(), 1));
            glDrawElements(GL_TRIANGLES, rawModel.getVerticesCount(), GL_UNSIGNED_INT, 0);

            unbind();
        }

        terrainShader.stop();
    }

    public void prepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getRawModel();

        // upload shader values
        terrainShader.uploadValue(shineDamperID, 1);
        terrainShader.uploadValue(reflectivityID, 0);

        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        bindTextures(terrain);
    }

    public void bindTextures(Terrain terrain){
        TerrainTexturePack pack = terrain.getTerrainTexturePack();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, pack.getBackgroundTexture().getID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, pack.getrTexture().getID());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, pack.getgTexture().getID());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, pack.getbTexture().getID());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getID());
    }

    public void unbind(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }
}
