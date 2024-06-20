package main.models;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class GuiTextureModel {

    private int ID;
    private boolean flip;
    private Vector2f scale;
    private String filePath;
    private Vector2f position;

    public GuiTextureModel(String filePath, Vector2f position, Vector2f scale, boolean flip){
        this.filePath = filePath;
        this.position = position;
        this.scale = scale;
        this.flip = flip;

        create();
    }

    private void create(){
        ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);


        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(flip);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null) {
            int channel = channels.get();
            if(channel == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);;
            }else{
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }

            stbi_image_free(image);
        }else{
            System.out.println("Unable to load texture: " + filePath);
        }
    }

    public int getID(){
        return ID;
    }

    public String getFilePath(){
        return filePath;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getPosition() {
        return position;
    }
}
