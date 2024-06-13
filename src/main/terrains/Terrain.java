package main.terrains;

import main.converter.Loader;
import main.models.RawModel;
import main.texture.TerrainTexture;
import main.texture.TerrainTexturePack;
import main.toolbox.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Terrain {

    private int size;
    private int maxHeight = 40;
    private int maxPixelVal = 256 * 256 * 256;
    private String heightMapPath;
    private float[][] heights;

    private float x, z;
    private RawModel rawModel;

    private TerrainTexture blendMap;
    private TerrainTexturePack terrainTexturePack;

    public Terrain(int x, int z, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMapPath){
        this.blendMap = blendMap;
        this.heightMapPath = heightMapPath;
        this.terrainTexturePack = terrainTexturePack;

        this.size = 800;
        this.rawModel = generateTerrain(heightMapPath);

        this.x = x * size;
        this.z = z * size;
    }

    private RawModel generateTerrain(String path){
        BufferedImage bufImage = null;
        try{
            bufImage = ImageIO.read(new File(path));
        }catch (Exception e){
            e.printStackTrace();
        }

        int vertexCount = bufImage.getHeight();
        heights = new float[vertexCount][vertexCount];

        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
        int vertexPointer = 0;
        for(int i=0;i<vertexCount;i++){
            for(int j=0;j<vertexCount;j++){
                vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * size;
                float height = getHeight(j, i, bufImage);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * size;
                Vector3f normal = calculateNormals(j, i, bufImage);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
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
        return Loader.getInstance().loadToVao(vertices, indices, textureCoords, normals);
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
            float terrainX = worldX - x;
            float terrainZ = worldZ - z;

            float gridSquareSize = size / ((float) heights.length - 1);
            int gridX = (int) Math.floor(terrainX / gridSquareSize);
            int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

            if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0){
                return 0;
            }

            float xCord = (terrainX % gridSquareSize) / gridSquareSize;
            float zCord = (terrainZ % gridSquareSize) / gridSquareSize;

            float answer;
            if(xCord <= (1-zCord)){
                answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0),
                        new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCord, zCord));
            }else{
                answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                        new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCord, zCord));
            }

            return answer;
    }

    public float getHeight(int x, int y, BufferedImage bufImage){
        if(x < 0 || y < 0 || x >= bufImage.getHeight() || y >= bufImage.getHeight()){
            return  0;
        }

        float height = bufImage.getRGB(x, y);
        height += (maxPixelVal / 2f);
        height /= (maxPixelVal / 2f);
        height *= maxHeight;

        return height;
    }

    public Vector3f calculateNormals(int x, int y, BufferedImage bufImage){

        float left = getHeight(x - 1, y, bufImage);
        float right = getHeight(x + 1, y, bufImage);
        float down = getHeight(x, y - 1 , bufImage);
        float up = getHeight(x, y + 1, bufImage);

        Vector3f normal = new Vector3f(left - right, 2f, down - up);
        normal.normalize();

        return normal;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public int getSize() {
        return size;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public float[][] getHeights() {
        return heights;
    }
}
