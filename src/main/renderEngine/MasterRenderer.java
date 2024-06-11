package main.renderEngine;

import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.ObjectModel;
import main.shaders.EntityShader;
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

    private EntityShader shader;
    private EntityRenderer entityRenderer;
    private Map<ObjectModel, List<Entity>> entities;

    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;
    private List<Terrain> terrainList;

    public MasterRenderer(){
        enableCulling();

        this.projectionMatrix = createProjectionMatrix();

        this.shader = new EntityShader();
        this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
        this.entities = new HashMap<>();

        this.terrainShader = new TerrainShader();
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.terrainList = new ArrayList<>();
    }

    public void render(Light light, Camera camera){
        prepare();
        shader.start();
        shader.uploadValue(lightColorID, light.getColor());
        shader.uploadValue(skyColorID, clearColor);
        shader.uploadValue(lightPositionID, light.getPosition());
        shader.uploadValue(viewID, Maths.createViewMatrix(camera));
        entityRenderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.uploadValue(skyColorID, clearColor);
        terrainShader.uploadValue(lightColorID, light.getColor());
        terrainShader.uploadValue(lightPositionID, light.getPosition());
        terrainShader.uploadValue(viewID, Maths.createViewMatrix(camera));
        terrainRenderer.render(terrainList);
        terrainShader.stop();

        terrainList.clear();
        entities.clear();
    }

    public void processEntity(Entity entity){
        ObjectModel objectModel = entity.getTExtureRawModel();
       if(!entities.containsKey(objectModel)){
           entities.put(objectModel, new ArrayList<Entity>());
       }

       entities.get(objectModel).add(entity);
    }

    public void prepare(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearColor(clearColor.x, clearColor.y, clearColor.z,1);
    }

    public void addTerrain(Terrain Terrain){
        terrainList.add(Terrain);
    }

    public void cleanUp(){
        shader.cleanUp();
    }

    public static void enableCulling(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling(){
        glDisable(GL_CULL_FACE);
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
