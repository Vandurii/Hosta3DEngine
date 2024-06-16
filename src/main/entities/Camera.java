package main.entities;

import main.controllers.MouseListener;
import org.joml.Vector2d;
import org.joml.Vector3f;

import static main.Configuration.defaultDistanceFromPlayer;
import static main.Configuration.defaultPitch;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private float yaw;
    private float roll;
    private float pitch;

    private Player player;
    private Vector3f position;
    private float distanceFromPlayer;
    private float angleAroundPlayer;

    private Vector2d lastCor;
    private Vector2d mouseDelta;
    private MouseListener mouse;

    public Camera(Player player){
        this.player = player;
        this.position = new Vector3f();

        init();
    }

    public Camera(Player player, Vector3f position){
        this.player = player;
        this.position = position;

        init();
    }

    public void init(){
        this.lastCor = new Vector2d();
        this.mouseDelta = new Vector2d();
        this.mouse = MouseListener.getInstance();

        this.distanceFromPlayer = defaultDistanceFromPlayer;
        this.pitch = defaultPitch;
    }

    public void update(float dt){
        Vector2d cor = mouse.getMousePos();
        mouseDelta = new Vector2d(cor.x - lastCor.x, cor.y - lastCor.y);

        calcZoom();
        calcPitch();
        calcAngle();

        float horDistance = calcHorizontal();
        float verDistance = calcVertical();
        calculateCamPos(horDistance, verDistance);
        yaw = 180 - (player.getRotation().y + angleAroundPlayer);

        lastCor = new Vector2d(cor.x, cor.y);
    }

    public void calcZoom(){
        distanceFromPlayer += (float) mouse.getScroll();
    }

    public void calcPitch(){
        if(mouse.isButtonPressed(GLFW_MOUSE_BUTTON_1)){
         //   if(pitch + mouseDelta.y < 10) return;;
            pitch += (float) mouseDelta.y * 0.1f;
        }
    }

    public void calcAngle(){
        if(mouse.isButtonPressed(GLFW_MOUSE_BUTTON_2)){
            angleAroundPlayer -= (float) mouseDelta.x * 0.1f;
        }
    }

    public float calcVertical(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    public float calcHorizontal(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    public void calculateCamPos(float horizontalDistance, float verticalDistance){
        float theta = player.getRotation().y + angleAroundPlayer;
        float thetaRad = (float) Math.toRadians(theta);
        float offsetX = (float) (horizontalDistance * Math.sin(thetaRad));
        float offsetZ = (float) (horizontalDistance * Math.cos(thetaRad));

        position.x = player.getPosition().x - offsetX;
        position.y = player.getPosition().y + verticalDistance;
        position.z = player.getPosition().z - offsetZ;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
