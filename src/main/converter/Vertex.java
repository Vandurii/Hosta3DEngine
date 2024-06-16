package main.converter;


import org.joml.Vector3f;

public class Vertex {

    private static final int NO_INDEX = -1;

    private int index;
    private int cordsIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;

    private float length;
    private Vector3f position;

    public Vertex(int index,Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    public int getIndex(){
        return index;
    }

    public boolean isSet(){
        return cordsIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther){
        return textureIndexOther== cordsIndex && normalIndexOther==normalIndex;
    }

    public void setCordsIndex(int cordsIndex){
        this.cordsIndex = cordsIndex;
    }

    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getCordsIndex() {
        return cordsIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

}

