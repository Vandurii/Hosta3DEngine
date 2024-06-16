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
import java.util.ArrayList;
import java.util.Arrays;

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

        int posArgCount = 3;
        int normalArgCount = 3;
        int textCordArgCount = 2;
        int indicesArgCount = 6;

        int triangleCountInRow = bufImage.getHeight();
        heights = new float[triangleCountInRow][triangleCountInRow];

        int totalTriangles = triangleCountInRow * triangleCountInRow;
        float[] vertices = new float[totalTriangles * posArgCount];
        float[] normals = new float[totalTriangles * normalArgCount];
        float[] textureCords = new float[totalTriangles*textCordArgCount];

        int index = 0;
        for(int x = 0; x < triangleCountInRow; x++){
            for(int z = 0;z < triangleCountInRow; z++){

                int offset = index  * posArgCount;

                float height = getHeight(z, x, bufImage);
                heights[z][x] = height;

                vertices[offset++] = (float)z/(triangleCountInRow - 1) * size;
                vertices[offset++] = height;
                vertices[offset] = (float)x/(triangleCountInRow - 1) * size;

                offset = index * normalArgCount;
                Vector3f normal = calculateNormals(z, x, bufImage);
                normals[offset++] = normal.x;
                normals[offset++] = normal.y;
                normals[offset] = normal.z;

                offset = index * textCordArgCount;
                textureCords[offset++] = (float)z/(triangleCountInRow - 1);
                textureCords[offset] = (float)x/(triangleCountInRow - 1);
                index++;
            }
        }

         index = 0;
        int pointer = 0;
        int[] indices = new int[indicesArgCount * (triangleCountInRow - 1) * (triangleCountInRow - 1)];
        for(int x = 0; x < triangleCountInRow - 1; x++){
            for(int z = 0; z < triangleCountInRow - 1; z++){

                int topLeft = (x * triangleCountInRow)+z;
                int topRight = topLeft + 1;
                int bottomLeft = ((x + 1) * triangleCountInRow)+z;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
                index++;
            }
        }

        return Loader.getInstance().loadToVao(vertices, indices, textureCords, normals);
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

