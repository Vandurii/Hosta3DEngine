package main.converter;

import main.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ObjectLoader {
    static List<Vertex> verticesList = new ArrayList<>();
    static List<Vector2f> cordsList = new ArrayList<>();
    static List<Vector3f> normalsList = new ArrayList<>();
    static List<Integer> indicesList = new ArrayList<>();

    static String verticesID = "v ";
    static String cordsID = "vt ";
    static String normalsID = "vn ";
    static String facesID = "f ";
    static String breakID = "s ";

    static int cordSize = 2;
    static int normalSize = 3;
    static int verticesSize = 3;

    public static RawModel loadOBJ(String path) {
        // Clear lists after previous use.
        reset();

        // Load data from file.
        loadDataFromFile(path);

        // Convert vertices, cords and normal to array.
        float[] verticesArray = new float[verticesList.size() * 3];
        float[] cordsArray = new float[verticesList.size() * cordSize];
        float[] normalsArray = new float[verticesList.size() * normalSize];
        convertDataToArrays(verticesArray, cordsArray, normalsArray);

        // Convert indices to array.
        int[] indicesArray = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++){
            indicesArray[i] = indicesList.get(i);
        }

        return Loader.getInstance().loadToVao(verticesArray, indicesArray, cordsArray, normalsArray);
    }

    public static void loadDataFromFile(String path){
        try (BufferedReader bufReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufReader.readLine()) != null) {

                String[] values = line.split(" ");
                float val1 = 0;
                float val2 = 0;
                float val3 = 0;

                try {
                    if (values.length > 1) val1 = Float.parseFloat(values[1]);
                    if (values.length > 2) val2 = Float.parseFloat(values[2]);
                    if (values.length > 3) val3 = Float.parseFloat(values[3]);
                }catch (Exception e){
//                    System.out.println(String.format("Can't convert line: %s from file: %s", line, path));
                }

                if (line.startsWith(verticesID)) {
                    int index = verticesList.size();
                    Vertex newVertex = new Vertex(index, new Vector3f(val1, val2, val3));
                    verticesList.add(newVertex);
                } else if (line.startsWith(cordsID)) {
                    cordsList.add(new Vector2f(val1, val2));
                } else if (line.startsWith(normalsID)) {
                    normalsList.add(new Vector3f(val1, val2, val3));
                } else if (line.startsWith(breakID)) {
                    break;
                }
            }

            while ((line = bufReader.readLine()) != null) {
                if(line.startsWith(facesID)) {
                    String[] faces = line.split(" ");

                    for (int i = 1; i < faces.length; i++){
                        initializeVertex(faces[i].split("/"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading the file");
        }
    }

    private static void initializeVertex(String[] vertex) {
        int index = Integer.parseInt(vertex[0]) - 1;
        int cordsIndex = Integer.parseInt(vertex[1]) - 1;
        int normalsIndex = Integer.parseInt(vertex[2]) - 1;

        Vertex currentVertex = verticesList.get(index);

        if (currentVertex.isInitialized()) {
            addTwin(currentVertex, cordsIndex, normalsIndex);
        } else {
            currentVertex.initialize(cordsIndex, normalsIndex);
            indicesList.add(index);
        }
    }

    private static void addTwin(Vertex vertex, int texCordsIndex, int normalIndex){
        int index = verticesList.size();
        // Check if exist object with this data, if not then create.
        Vertex twin = vertex.addTwin(index, texCordsIndex, normalIndex, vertex.getPosition());

        // If there was created new object add it to verticesList and load new index to the indicesList.
        // If there wasn't created new object load old index.
        if(twin != null){
            verticesList.add(twin);
            indicesList.add(twin.getIndex());
        }else{
            indicesList.add(vertex.getIndex());
        }
    }

    private static void convertDataToArrays(float[] verticesArray, float[] cordsArray, float[] normalsArray) {
        for (int i = 0; i < verticesList.size(); i++) {
            Vertex currentVertex = verticesList.get(i);

            Vector3f position = currentVertex.getPosition();
            Vector2f textureCords = cordsList.get(currentVertex.getCordsIndex());
            Vector3f normalVector = normalsList.get(currentVertex.getNormalIndex());

            int offset = i * verticesSize;
            verticesArray[offset] = position.x;
            verticesArray[offset + 1] = position.y;
            verticesArray[offset + 2] = position.z;

            offset = i * cordSize;
            cordsArray[offset] = textureCords.x;
            cordsArray[offset + 1] = 1 - textureCords.y;

             offset = i * normalSize;
            normalsArray[offset] = normalVector.x;
            normalsArray[offset + 1] = normalVector.y;
            normalsArray[offset + 2] = normalVector.z;
        }
    }

    public static void reset(){
        verticesList.clear();
        cordsList.clear();
        normalsList.clear();
        indicesList.clear();
    }
}