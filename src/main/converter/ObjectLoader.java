package main.converter;

import main.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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
        reset();


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
                    System.out.println(String.format("Can't convert line: %s from file: %s", line, path));
                }

                if (line.startsWith(verticesID)) {
                    Vertex newVertex = new Vertex(verticesList.size(), new Vector3f(val1, val2, val3));
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
                        processVertex(faces[i].split("/"));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading the file");
        }

        float[] verticesArray = new float[verticesList.size() * 3];
        float[] cordsArray = new float[verticesList.size() * cordSize];
        float[] normalsArray = new float[verticesList.size() * normalSize];
        convertDataToArrays(verticesArray, cordsArray, normalsArray);

        // Generate Indices
        int[] indicesArray = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++){
            indicesArray[i] = indicesList.get(i);
        }

        return Loader.getInstance().loadToVao(verticesArray, indicesArray, cordsArray, normalsArray);
    }

    public static void reset(){
        verticesList.clear();
        cordsList.clear();
        normalsList.clear();
        indicesList.clear();
    }

    private static void processVertex(String[] vertex) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = verticesList.get(index);
        int cordsIndex = Integer.parseInt(vertex[1]) - 1;
        int normalsIndex = Integer.parseInt(vertex[2]) - 1;

        if (!currentVertex.isSet()) {
            currentVertex.setCordsIndex(cordsIndex);
            currentVertex.setNormalIndex(normalsIndex);
            indicesList.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, cordsIndex, normalsIndex);
        }
    }

    private static void convertDataToArrays(float[] verticesArray, float[] cordsArray, float[] normalsArray) {


        for (int i = 0; i < verticesList.size(); i++) {
            Vertex currentVertex = verticesList.get(i);

            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = cordsList.get(currentVertex.getCordsIndex());
            Vector3f normalVector = normalsList.get(currentVertex.getNormalIndex());

            int offset = i * verticesSize;
            verticesArray[offset] = position.x;
            verticesArray[offset + 1] = position.y;
            verticesArray[offset + 2] = position.z;

            offset = i * cordSize;
            cordsArray[offset] = textureCoord.x;
            cordsArray[offset + 1] = 1 - textureCoord.y;

             offset = i * normalSize;
            normalsArray[offset] = normalVector.x;
            normalsArray[offset + 1] = normalVector.y;
            normalsArray[offset + 2] = normalVector.z;
        }
    }

    private static void dealWithAlreadyProcessedVertex(Vertex vertex, int textureIndex, int newNormalIndex) {
        if (vertex.hasSameTextureAndNormal(textureIndex, newNormalIndex)) {
            indicesList.add(vertex.getIndex());
        } else {
            Vertex anotherVertex = vertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, textureIndex, newNormalIndex);
            } else {
                Vertex duplicateVertex = new Vertex(verticesList.size(), vertex.getPosition());
                duplicateVertex.setCordsIndex(textureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                vertex.setDuplicateVertex(duplicateVertex);

                verticesList.add(duplicateVertex);
                indicesList.add(duplicateVertex.getIndex());
            }
        }
    }
}