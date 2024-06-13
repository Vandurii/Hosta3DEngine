package main.entities;

import main.models.ObjectModel;
import org.joml.Vector3f;

import javax.swing.text.Position;

public class Entity {
    private float scale;
    private Vector3f position;
    private Vector3f rotation;
    private ObjectModel objectModel;

    public Entity(ObjectModel objectModel, Vector3f position, Vector3f rotation, float scale){
        this.objectModel = objectModel;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(ObjectModel objectModel){
        this.objectModel = objectModel;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
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
