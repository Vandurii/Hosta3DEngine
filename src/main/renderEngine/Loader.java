package main.renderEngine;

import main.models.RawModel;
import main.models.TextureModel;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    private static int attribIndex;

    public RawModel loadToVao(float[] vertices, int[] indices, float[] texCords, float[] normals){
        int vaoID = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(vertices, 3);
        storeDataInAttributeList(texCords, 2);
        storeDataInAttributeList(normals, 3);
        unbindVao();
        resetAttribIndex();

        return new RawModel(vaoID, indices.length);
    }

    public int createVao(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);

        return vaoID;
    }

    public TextureModel loadTexture(String path, boolean flip){
        TextureModel textureModel = new TextureModel(path, flip);
        textures.add(textureModel.getTexID());

        return textureModel;
    }

    public TextureModel loadTexture(String path, boolean flip, float dampVal, float refVal){
        TextureModel textureModel = new TextureModel(path, flip, dampVal, refVal);
        textures.add(textureModel.getTexID());

        return textureModel;
    }

    public TextureModel loadTexture(String path, boolean flip, boolean transparency, boolean fakeLightning){
        TextureModel textureModel = new TextureModel(path, flip, transparency, fakeLightning);
        textures.add(textureModel.getTexID());

        return textureModel;
    }

    public TextureModel loadTexture(String path, boolean flip, float dampVal, float refVal, boolean transparency, boolean fakeLightning ){
        TextureModel textureModel = new TextureModel(path, flip, dampVal, refVal, transparency, fakeLightning);
        textures.add(textureModel.getTexID());

        return textureModel;
    }

    public void storeDataInAttributeList(float[] data, int size){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attribIndex++, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bindIndicesBuffer(int[] indices){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    public void resetAttribIndex(){
        attribIndex = 0;
    }

    public void unbindVao(){
        glBindVertexArray(0);
    }

    public void cleanUp(){
        for(Integer vao: vaos){
            glDeleteVertexArrays(vao);
        }

        for(Integer vbo: vbos){
            glDeleteBuffers(vbo);
        }

        for(Integer tex: textures){
            glDeleteTextures(tex);
        }
    }
}
