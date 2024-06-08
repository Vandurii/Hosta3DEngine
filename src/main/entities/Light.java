package main.entities;

import org.joml.Vector3f;

public class Light {
    private Vector3f color;
    private Vector3f position;

    public Light(Vector3f position, Vector3f color){
        this.position = position;
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getPosition() {
        return position;
    }
}
