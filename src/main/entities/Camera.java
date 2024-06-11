package main.entities;

import main.controller.MouseListener;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

public class Camera {
    public static float pitch = 20;
    public static float distanceFromPlayer = 50;

    private float yaw;
    private float roll;
    public  float angelAroundPlayer;

    private Player player;
    private static Vector3f position;

    private Vector2d lastCor;
    private Vector2d mouseDelta;

    public Camera(Player player){
        this.player = player;
        Camera.position = new Vector3f();
        this.lastCor = new Vector2d();
        this.mouseDelta = new Vector2d();
    }

    public void update(float dt){
        Vector2d cor = MouseListener.getInstance().getMouse();
        mouseDelta = new Vector2d(cor.x - lastCor.x, cor.y - lastCor.y);

        calcZoom();
        calcPitch(dt);
        calcAngle(dt);

        float horizontal = calculateHorizontal();
        float vertical = calculateVertical();

        calculateCamPos(horizontal, vertical);
        yaw = 180 - (player.getRotation().y + angelAroundPlayer);
        lastCor = new Vector2d(cor.x, cor.y);
    }

    public void calcPitch(float dt){
        if(MouseListener.getInstance().isButtonPressed(GLFW_MOUSE_BUTTON_2)) {
            pitch += (float) mouseDelta.y * 0.1f;
        }
    }

    public void calcZoom(){
            distanceFromPlayer += (float) MouseListener.getInstance().getScroll();
    }

    public void calcAngle(float dt){
        if(MouseListener.getInstance().isButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            angelAroundPlayer -= (float) mouseDelta.x * 0.1f;
        }
    }

    public float calculateHorizontal(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    public float calculateVertical(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    public void calculateCamPos(float horizontalDistance, float verticalDistance){
        float theta = player.getRotation().y + angelAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    public  float getPitch() {
        return pitch;
    }

    public  float getYaw() {
        return yaw;
    }

    public Vector3f getPosition() {
        return position;
    }
}
