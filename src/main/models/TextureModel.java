package main.models;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureModel {
    private String filePath;
    private int ID;
    private int width, height;
    private boolean flip;

    private float shineDamper;
    private float reflectivity;

    private boolean hasTransparency;
    private boolean hasFakeLightning;

    public TextureModel(String filePath, boolean flip){
        this.shineDamper = defaultShineDamper;
        this.reflectivity = defaultReflectivity;
        this.filePath = filePath;
        this.flip = flip;

        create();
    }

    public TextureModel(String filePath, boolean flip, float shineDamper, float reflectivity){
        this.reflectivity = reflectivity;
        this.shineDamper = shineDamper;
        this.filePath = filePath;
        this.flip = flip;

        create();
    }

    public TextureModel(String filePath, boolean flip, boolean hasTransparency, boolean hasFakeLightning){
        this.hasFakeLightning = hasFakeLightning;
        this.hasTransparency = hasTransparency;
        this.reflectivity = defaultReflectivity;
        this.shineDamper = defaultShineDamper;
        this.filePath = filePath;
        this.flip = flip;

        create();
    }

    public TextureModel(String filePath, boolean flip, float shineDamper, float reflectivity, boolean hasTransparency, boolean hasFakeLightning){
        this.reflectivity = defaultReflectivity;
        this.shineDamper = defaultShineDamper;
        this.hasFakeLightning = hasFakeLightning;
        this.hasTransparency = hasTransparency;
        this.filePath = filePath;
        this.flip = flip;

        create();
    }


    private void create(){

        ID = glGenTextures();
    //    glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//       if(!filePath.equals(lowPolyTreeImagePath)) {
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
//        }else{
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
 //       }

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

    public int getID(){
        return ID;
    }

    public String getFilePath(){
        return filePath;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean hasFakeLightning() {
        return hasFakeLightning;
    }

    public void setFakeLightning(boolean hasFakeLightning) {
        this.hasFakeLightning = hasFakeLightning;
    }
}
