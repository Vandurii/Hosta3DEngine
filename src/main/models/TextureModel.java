package main.models;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureModel {
    private String filePath;
    private int ID;
    private int width, height;
    private int numberOfRows;
    private boolean flip;

    private float shineDamper;
    private float reflectivity;

    private boolean hasTransparency;
    private boolean hasFakeLightning;

    private TextureModel(Builder builder){
        this.flip = builder.flip;
        this.filePath = builder.filePath;
        this.shineDamper = builder.shineDamper;
        this.numberOfRows = builder.numberOfRows;
        this.reflectivity = builder.reflectivity;
        this.hasTransparency = builder.hasTransparency;
        this.hasFakeLightning = builder.hasFakeLightning;

        create();
    }

    public static class Builder{
        // required
        private String filePath;

        // optional
        private int numberOfRows;

        private float shineDamper;
        private float reflectivity;

        private boolean flip;
        private boolean hasTransparency;
        private boolean hasFakeLightning;

        public Builder(String filePath){
            this.filePath = filePath;
            this.numberOfRows = 1;
        }

        public Builder shineDamper(float val){
            this.shineDamper = val;
            return this;
        }

        public Builder reflectivity(float val){
            this.reflectivity = val;
            return this;
        }

        public Builder flip(){
            flip = true;
            return  this;
        }

        public Builder transparent(){
            hasTransparency = true;
            return  this;
        }

        public Builder fakeLighted(){
            hasFakeLightning = true;
            return this;
        }

        public Builder rowsNumber(int numberOfRows){
            this.numberOfRows = numberOfRows;
            return this;
        }

        public TextureModel build(){
            return new TextureModel(this);
        }
    }

    private void create(){
        glGenerateMipmap(GL_TEXTURE_2D);
        ID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);



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

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public boolean hasFakeLightning() {
        return hasFakeLightning;
    }

    public int getNumberOfRows(){
        return numberOfRows;
    }
}
