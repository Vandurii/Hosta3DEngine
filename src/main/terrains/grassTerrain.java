package main.terrains;

import main.models.RawModel;
import main.renderEngine.Loader;
import main.models.TextureModel;

public class grassTerrain {
    float x;
    float z;

    private float size;
    private int vertexCount;
    private RawModel rawModel;
    private TextureModel textureModel;

    public grassTerrain(int x, int z, Loader loader, TextureModel textureModel){
        this.size = 800;
        this.vertexCount =128;
        this.rawModel = generateTerrain(loader);
        this.textureModel = textureModel;

        this.x = x * size;
        this.z = z * size;
    }

    private RawModel generateTerrain(Loader loader){
        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
        int vertexPointer = 0;
        for(int i=0;i<vertexCount;i++){
            for(int j=0;j<vertexCount;j++){
                vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * size;
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * size;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/((float)vertexCount - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<vertexCount-1;gz++){
            for(int gx=0;gx<vertexCount-1;gx++){
                int topLeft = (gz*vertexCount)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*vertexCount)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVao(vertices, indices, textureCoords, normals);
    }


    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getTextureModel() {
        return textureModel;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
}