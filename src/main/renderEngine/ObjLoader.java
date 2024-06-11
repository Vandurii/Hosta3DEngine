package main.renderEngine;

import main.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {
    private List<Float> verticesList;
    private List<Vector2f> cordsList;
    private List<Vector3f> normalsList;
    private List<Integer> indicesList;

    private float[] cordsArray;
    private float[] normalsArray;

    private String verticesID;
    private String cordsID;
    private String normalsID;
    private String facesID;
    private String breakID;

    private int cordSize;
    private int normalSize;

    public ObjLoader() {
        this.verticesID = "v ";
        this.cordsID = "vt ";
        this.normalsID = "vn ";
        this.facesID = "f ";
        this.breakID = "s 1";

        this.cordSize = 2;
        this.normalSize = 3;

        this.verticesList = new ArrayList<>();
        this.cordsList = new ArrayList<>();
        this.normalsList = new ArrayList<>();
        this.indicesList = new ArrayList<>();
    }

    public RawModel loadObjModel(String path, Loader loader) {
        try (BufferedReader bufReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufReader.readLine()) != null) {

                String[] values = line.split(" ");
                float val1 = 0;
                float val2 = 0;
                float val3 = 0;

                if (line.startsWith(verticesID)) {
                    val1 = Float.parseFloat(values[1]);
                    val2 = Float.parseFloat(values[2]);
                    val3 = Float.parseFloat(values[3]);
                    verticesList.add(val1);
                    verticesList.add(val2);
                    verticesList.add(val3);

                    // make vec 3 instead that
                } else if (line.startsWith(cordsID)) {
                    val1 = Float.parseFloat(values[1]);
                    val2 = Float.parseFloat(values[2]);
                    cordsList.add(new Vector2f(val1, val2));
                } else if (line.startsWith(normalsID)) {
                    val1 = Float.parseFloat(values[1]);
                    val2 = Float.parseFloat(values[2]);
                    val3 = Float.parseFloat(values[3]);
                    normalsList.add(new Vector3f(val1, val2, val3));
                } else if (line.startsWith(breakID)) {
                    cordsArray = new float[verticesList.size() * cordSize];
                    normalsArray = new float[verticesList.size() * normalSize];
                    break;
                }
            }

            while ((line = bufReader.readLine()) != null) {
                if(line.startsWith(facesID)) {
                    String[] faces = line.split(" ");

                    for (int i = 1; i < faces.length; i++){
                        processVertex(faces[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] indicesArray = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++){
            indicesArray[i] = indicesList.get(i);
        }

        float[] verticesArray = new float[verticesList.size()];
        for(int i = 0; i < verticesList.size(); i++){
            verticesArray[i] = verticesList.get(i);
        }

        return loader.loadToVao(verticesArray, indicesArray, cordsArray, normalsArray);
    }

    public void processVertex(String face){
        String[] faces = face.split("/");

        // data from String
        int verticesIndex = Integer.parseInt(faces[0]) - 1;
        int cordsIndex = Integer.parseInt(faces[1]) - 1;
        int normalIndex = Integer.parseInt(faces[2]) - 1;

        indicesList.add(verticesIndex);

        // Cords
        Vector2f thisCords = cordsList.get(cordsIndex);
        int offset = verticesIndex * cordSize;
        cordsArray[offset] = thisCords.x;
        cordsArray[offset + 1] = thisCords.y;

        // Normals
        Vector3f thisNormal = normalsList.get(normalIndex);
        offset = verticesIndex * normalSize;
        normalsArray[offset] = thisNormal.x;
        normalsArray[offset + 1] = thisNormal.y;
        normalsArray[offset + 2] = thisNormal.z;
    }
}
