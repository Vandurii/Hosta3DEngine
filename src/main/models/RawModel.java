package main.models;

public class RawModel {
    private int vaoID;
    private int verticesCount;

    public RawModel(int vaoID, int verticesCount){
        this.vaoID = vaoID;
        this.verticesCount = verticesCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVerticesCount() {
        return verticesCount;
    }
}
