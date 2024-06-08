package main.renderEngine;

import main.models.RawModel;
import main.textures.TextureModel;
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

    public RawModel loadToVao(float[] vertices, int[] indices, float[] texCords, float[] normals){
        int vaoID = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, vertices, 3);
        storeDataInAttributeList(1, texCords, 2);
        storeDataInAttributeList(2, normals, 3);
        unbindVao();

        return new RawModel(vaoID, indices.length);
    }

    public int createVao(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);

        return vaoID;
    }

    public TextureModel loadTexture(String path){
        TextureModel textureModel = new TextureModel(path, false);
        textures.add(textureModel.getTexID());

        return textureModel;
    }

    public void storeDataInAttributeList(int attribNumber, float[] data, int size){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer floatBuffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attribNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bindIndicesBuffer(int[] indices){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer intBuffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW);
    }

    public FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();

        return floatBuffer;
    }

    public IntBuffer storeDataInIntBuffer(int[] indices){
        IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);
        intBuffer.put(indices);
        intBuffer.flip();

        return intBuffer;
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
