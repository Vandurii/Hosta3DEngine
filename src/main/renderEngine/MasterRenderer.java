package main.renderEngine;

import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.TextureRawModel;
import main.shaders.StaticShader;
import main.shaders.TerrainShader;
import main.terrains.Terrain;
import main.tollbox.Maths;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class MasterRenderer {
    private Matrix4f projectionMatrix;

    private StaticShader shader;
    private EntityRenderer entityRenderer;
    private Map<TextureRawModel, List<Entity>> entities;

    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;
    private List<Terrain> terrainList;

    public MasterRenderer(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        this.projectionMatrix = createProjectionMatrix();

        this.shader = new StaticShader();
        this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
        this.entities = new HashMap<>();

        this.terrainShader = new TerrainShader();
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.terrainList = new ArrayList<>();
    }

    public void render(Light light, Camera camera){
        prepare();
        shader.start();
        shader.uploadValue("lightColor", light.getColor());
        shader.uploadValue("lightPos", light.getPosition());
        shader.uploadValue("viewMatrix", Maths.createViewMatrix(camera));
        entityRenderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.uploadValue("lightColor", light.getColor());
        terrainShader.uploadValue("lightPos", light.getPosition());
        terrainShader.uploadValue("viewMatrix", Maths.createViewMatrix(camera));
        terrainRenderer.render(terrainList);
        terrainShader.stop();

        terrainList.clear();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TextureRawModel textureRawModel = entity.getTExtureRawModel();
       if(!entities.containsKey(textureRawModel)){
           entities.put(textureRawModel, new ArrayList<Entity>());
       }

       entities.get(textureRawModel).add(entity);
    }

    public void prepare(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearColor(clearColor.x, clearColor.y, clearColor.z,1);
    }

    public void addTerrain(Terrain terrain){
        terrainList.add(terrain);
    }

    public void cleanUp(){
        shader.cleanUp();
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
