package main.entities;

import org.joml.Vector3f;

public class Camera {
    private float pitch;
    private float yaw;
    private float roll;

    private static Vector3f position;

    public Camera(){
        Camera.position = new Vector3f();
    }

    public static void move(Direction direction){
        float val = 0.8f;
        switch (direction){
            case Up -> position.y += val;
            case Down ->  position.y -= val;
            case Left -> position.x -= val;
            case Right ->  position.x += val;
            case backward -> position.z -= val;
            case forward ->  position.z += val;
        }
    }

    public  float getPitch() {
        return pitch;
    }

    public  void setPitch(float pitch) {
       pitch = pitch;
    }

    public  float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
       yaw = yaw;
    }

    public  float getRoll() {
        return roll;
    }

    public  void setRoll(float roll) {
       roll = roll;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
