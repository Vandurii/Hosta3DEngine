package main.entities;

import main.models.ObjectModel;
import org.joml.Vector3f;

public class Entity {
    private ObjectModel objectModel;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Entity(ObjectModel objectModel, Vector3f position, Vector3f rotation, float scale){
        this.objectModel = objectModel;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void increasePosition(Vector3f value){
        increase(position, value);
    }

    public void decreasePosition(Vector3f value){
        decrease(position, value);
    }

    public void increaseRotation(Vector3f value){
        increase(rotation, value);
    }

    public void decreaseRotation(Vector3f value){
        decrease(rotation, value);
    }

    public void increase(Vector3f source, Vector3f value){
        source.x += value.x;
        source.y += value.y;
        source.z += value.z;
    }

    public void decrease(Vector3f source, Vector3f value){
        source.x -= value.x;
        source.y -= value.y;
        source.z -= value.z;
    }

    public ObjectModel getTExtureRawModel() {
        return objectModel;
    }

    public void TextureRawModel(ObjectModel objectModel) {
        this.objectModel = objectModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
