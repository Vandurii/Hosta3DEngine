package main.entities;

import main.models.ObjectModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Entity {
    private float scale;
    private Vector3f position;
    private Vector3f rotation;
    private int textureIndex;
    private ObjectModel objectModel;

    public Entity(ObjectModel objectModel, Vector3f position, Vector3f rotation,int textureIndex, float scale){
        this.textureIndex = textureIndex;
        this.objectModel = objectModel;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }


    public Entity(ObjectModel objectModel, Vector3f position, Vector3f rotation, float scale){
        this.textureIndex = 0;
        this.objectModel = objectModel;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(ObjectModel objectModel){
        this.objectModel = objectModel;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.textureIndex = 0;
        this.scale = 1;
    }

    public void increasePosition(Vector3f val){
        increase(position, val);
    }

    public void increaseRotation(Vector3f val){
        increase(rotation, val);
    }

    private void increase(Vector3f target, Vector3f val){
        target.x += val.x;
        target.y += val.y;
        target.z += val.z;
    }

//    public float getTextureXOffset(){
//        int rows = objectModel.getTextureModel().getNumberOfRows();
//
//        float column = (float) textureIndex % rows;
//        return  column / rows;
//    }
//
//    public float getTextureYOffset(){
//        int rows = objectModel.getTextureModel().getNumberOfRows();
//        float column = (float) textureIndex / rows;
//
//        return  column / rows;
//    }

    public float getTextureXOffset(){
        System.out.println(textureIndex);
        int column =  textureIndex % getObjectModel().getTextureModel().getNumberOfRows();
        return  (float)column / (float)getObjectModel().getTextureModel().getNumberOfRows();
    }

    public float getTextureYOffset(){
        int column =  textureIndex / getObjectModel().getTextureModel().getNumberOfRows();

        return (float) column / (float)getObjectModel().getTextureModel().getNumberOfRows();
    }



    public Vector2f getTextureOffset(){
       // System.out.println(String.format("x: %.2f y: %.2f", getTextureXOffset(), getTextureYOffset()));
        return new Vector2f(getTextureXOffset(), getTextureYOffset());
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public ObjectModel getObjectModel() {
        return objectModel;
    }
}
