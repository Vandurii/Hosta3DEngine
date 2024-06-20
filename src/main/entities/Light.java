package main.entities;

import org.joml.Vector3f;

public class Light {
    private Vector3f[] position;
    private Vector3f[] color;
    private Vector3f[] attenuation;

    public Light(Vector3f[] position, Vector3f[] color){
        if(position.length != color.length) throw new IllegalStateException("The color array and position array length must be equal.");
        this.position = new Vector3f[position.length];
        this.color = new Vector3f[color.length];
        this.attenuation = new Vector3f[position.length];

        for(int i = 0; i < position.length; i++) {
            this.position[i] = position[i];
            this.color[i] = color[i];
            this.attenuation[i] = new Vector3f(1, 1f, 1f);
        }
    }

    public Light(Vector3f[] position, Vector3f[] color, Vector3f[] attenuation){
        if(position.length != color.length || position.length != attenuation.length) throw new IllegalStateException("The color array, position array and attenuation array length must be equal.");
        this.position = new Vector3f[position.length];
        this.color = new Vector3f[color.length];
        this.attenuation = new Vector3f[position.length];

        for(int i = 0; i < position.length; i++) {
            this.position[i] = position[i];
            this.color[i] = color[i];
            this.attenuation[i] = attenuation[i];
        }
    }

    public Vector3f[] getPosition() {
        return position;
    }

    public Vector3f[] getColor() {
        return color;
    }

    public Vector3f[] getAttenuation() {
      //  System.out.println(String.format("x:%s y:%s z:%s", attenuation[0].x, attenuation[0].y, attenuation[0].z));
        return attenuation;
    }
}
