package main.models;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.stb.STBImage.*;

public class TextureCubeModel {

    private int ID;
    private boolean flip;
    private String[] filesPath;
    private int width, height;

    public TextureCubeModel(String[] filesPath, boolean flip){
        this.flip = flip;
        this.filesPath = filesPath;

        create();
    }

    private void create(){
        ID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, ID);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(flip);

        for(int i = 0; i < filesPath.length; i++) {
            ByteBuffer image = stbi_load(filesPath[i], width, height, channels, 0);

            if (image != null) {
                this.width = width.get(0);
                this.height = height.get(0);

                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);

            } else {
                System.out.println("Unable to load texture: " + filesPath[i]);
            }


//            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_REPEAT);
//            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            stbi_image_free(image);
        }


    }

    public int getID(){
        return ID;
    }

    public String[] getFilesPath(){
        return filesPath;
    }
}
