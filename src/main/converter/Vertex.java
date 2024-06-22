package main.converter;


import org.joml.Vector3f;

import static main.Configuration.NO_INDEX;

public class Vertex {

    private int index;
    private int cordsIndex;
    private int normalIndex;
    private Vertex twinVertex;

    private Vector3f position;

    public Vertex(int index, Vector3f position){
        this.cordsIndex = NO_INDEX;
        this.normalIndex = NO_INDEX;
        this.index = index;
        this.position = position;
    }

    public Vertex addTwin(int index, int texCordsIndex, int normalIndex, Vector3f position){
        // Return null if texture and normal index are equal with this object or with twin object from this object
        if(!hasSameTextureAndNormal(texCordsIndex, normalIndex)){
            if(twinVertex == null){
                Vertex twin = new Vertex(index, position);
                twin.initialize(cordsIndex, normalIndex);
                twinVertex = twin;
                return twin;
            }else{
                return twinVertex.addTwin(index, cordsIndex, normalIndex, position);
            }
        }

        return null;
    }

    public void initialize(int cordsIndex, int normalIndex){
        this.cordsIndex = cordsIndex;
        this.normalIndex = normalIndex;
    }

    public boolean isInitialized(){
        return cordsIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    public boolean hasSameTextureAndNormal(int cordsIndex, int normalIndex){
        return cordsIndex == this.cordsIndex && normalIndex == this.normalIndex;
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

    public int getIndex(){
        return index;
    }
}

