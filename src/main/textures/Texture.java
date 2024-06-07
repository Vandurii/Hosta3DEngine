package main.textures;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture{
    private String filePath;
    private int texID;
    private int width, height;
    private boolean flip;

    public Texture(String filePath, boolean flip){
        this.filePath = filePath;
        this.flip = flip;

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // GL NEAREST BOTH OF THEM // GL_LINEAR

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(flip);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

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

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getTexID(){
        return texID;
    }

    public String getFilePath(){
        return filePath;
    }
}
