package main.entities;

import main.models.ObjectModel;
import main.terrains.Terrain;
import org.joml.Vector3f;

import static main.Configuration.*;

public class Player extends Entity{
    public static float currentSpeed;
    public static float currentTurnAngle;
    public static float upwardSpeed;

    public static float terrainHeight;
    private Terrain terrain;

    public Player(ObjectModel objectModel, Vector3f position, Vector3f rotation, float scale){
        super(objectModel, position, rotation, scale);
    }

    public void update(float dt, Terrain terrain){
        this.terrain = terrain;
        //System.out.println(String.format("x: %s y: %s", getPosition().x, getPosition().z));;
         terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);

        float distance = currentSpeed * dt;
        double rotY = Math.toRadians(getRotation().y);
        float dx = (float) (distance * Math.sin(rotY));
        float dz = (float) (distance * Math.cos(rotY));
        increasePosition(new Vector3f(dx, 0, dz));

        increaseRotation(new Vector3f(0, currentTurnAngle * dt, 0));

        // Decrease y value if the value is greater the 0.
        if(upwardSpeed != 0) {
            increasePosition(new Vector3f(0, upwardSpeed, 0));

            // Set y val to 0, if it is less, set upward val to 0
            if(getPosition().y <= terrainHeight){
                getPosition().y = terrainHeight;
                upwardSpeed = 0;
            }
            upwardSpeed += gravity * dt;
        }else if(getPosition().y >= terrainHeight){
            upwardSpeed -= jumpPower;
        }
    }

    public void jump(){
        if(getPosition().y <= terrain.getHeightOfTerrain(getPosition().x, getPosition().z)){
            upwardSpeed = jumpPower;
        }
    }

    public Terrain getTerrain(){
        return  terrain;
    }
}
