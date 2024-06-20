package main.converter;

import main.models.RawModel;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.*;

public class Loader {

    private static int attributeIndex;
    private static Loader instance;

    private List<Integer> vaoList;
    private List<Integer> vboList;
    private List<Integer> texturesList;

    private Loader(){
        this.vaoList = new ArrayList<>();
        this.vboList = new ArrayList<>();
        this.texturesList = new ArrayList<>();
    }

    public RawModel loadToVao(float[] vertices, int[] indices, float[] texCords, float[] normals){
        int vaoID = createVAO();
        createEBO(indices);
        createVBO(vertices, 3);
        createVBO(texCords, 2);
        createVBO(normals, 3);
        resetAttribIndex();
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVao(float[] position){
        int vaoID = createVAO();
        createVBO(position, 2);
        resetAttribIndex();
        unbindVAO();

        return new RawModel(vaoID, position.length/ 2);
    }

    public int createVAO(){
        int vao = glGenVertexArrays();
        vaoList.add(vao);
        glBindVertexArray(vao);

        return vao;
    }

    public void unbindVAO(){
        glBindVertexArray(0);
    }

    public void createEBO(int[] indices){
        int eboID = glGenBuffers();
        vboList.add(eboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    public void createVBO(float[] data, int size){
        int vboID = glGenBuffers();
        vboList.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // increase index, so that next buffer has different index
        attributeIndex++;
    }

    public static void resetAttribIndex(){
        attributeIndex = 0;
    }

    public void destroy(){
        for(Integer vao: vaoList){
            glDeleteVertexArrays(vao);
        }

        for(Integer vbo: vboList){
            glDeleteBuffers(vbo);
        }

        for(Integer tex: texturesList){
            glDeleteTextures(tex);
        }
    }

    public static Loader getInstance(){
        if(instance == null) instance = new Loader();

        return instance;
    }
}
