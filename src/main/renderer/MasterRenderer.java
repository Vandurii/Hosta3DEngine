package main.renderer;

import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.ObjectModel;
import main.models.RawModel;
import main.shaders.EntityShader;
import main.shaders.TerrainShader;
import main.terrains.Terrain;
import main.toolbox.Maths;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {

    private Matrix4f projection;
    private static MasterRenderer instance;

    private EntityShader entityShader;
    private EntityRenderer entityRenderer;

    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;

    private List<Terrain> terrainList;
    private Map<ObjectModel, List<Entity>> entitiesList;

    private MasterRenderer(){
        this.projection = Maths.createProjection();

        this.entitiesList = new HashMap<>();
        this.terrainList = new ArrayList<>();

        this.terrainShader = new TerrainShader(terrainVertexShaderPath, terrainFragmentShaderPath);
        this.terrainRenderer = new TerrainRenderer(terrainShader, projection);

        this.entityShader = new EntityShader(entityVertexShaderPath, entityFragmentShaderPath);
        this.entityRenderer = new EntityRenderer(entityShader, projection);
    }

    public void render(Camera camera, Light light){
        // Prepare screen.
        prepare();

        // Render
        entityRenderer.render(entitiesList, camera, light);
        terrainRenderer.render(terrainList, camera, light);

        // clear
        entitiesList.clear();
        terrainList.clear();
    }

    public void processEntity(Entity entity){
        ObjectModel objectModel = entity.getObjectModel();
        if(!entitiesList.containsKey(objectModel)){
            entitiesList.put(objectModel, new ArrayList<>());
        }

        entitiesList.get(objectModel).add(entity);
    }

    public void processTerrain(Terrain terrain){
        terrainList.add(terrain);
    }

    private void prepare(){
        // Enable depth test
        glEnable(GL_DEPTH_TEST);

        // Set the clear color
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);

        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    protected void enableCulling(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    protected void disableCulling(){
        glDisable(GL_CULL_FACE);
    }

    public static MasterRenderer getInstance(){
        // return instance, initialize it when it isn't initialized.
        if(instance == null) instance = new MasterRenderer();

        return instance;
    }
}
