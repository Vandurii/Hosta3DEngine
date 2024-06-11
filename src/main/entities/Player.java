package main.entities;

import main.models.ObjectModel;
import org.joml.Vector3f;

import static main.Configuration.*;

public class Player extends Entity{

    public static float currentSpeed;
    public static float currentTurnSpeed;
    public static float terrainHeight = terrainYVal;
    public static float upwardsSpeed;

    public Player(ObjectModel objectModel, Vector3f position, Vector3f rotation, float scale) {
        super(objectModel, position, rotation, scale);
    }

    public void move(float dt){
        // turn out
        increaseRotation(new Vector3f(0, currentTurnSpeed * dt, 0));

        // run
        float distance = currentSpeed * dt;
        float dx = (float) (distance * Math.sin(Math.toRadians(getRotation().y)));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRotation().y)));
        increasePosition(new Vector3f(dx, 0, dz));

        // jump
        upwardsSpeed += gravity * dt;
        increasePosition(new Vector3f(0, upwardsSpeed * dt, 0));

        if(getPosition().y <= terrainHeight){
            setPosition(new Vector3f(getPosition().x, terrainHeight, getPosition().z));
            upwardsSpeed = 0;
        }
    }

    public void jump(){
        if(getPosition().y <= terrainHeight){
            upwardsSpeed = jumpPower;
        }
    }
}
