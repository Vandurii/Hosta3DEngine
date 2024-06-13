package main.controllers;

import main.components.Component;
import main.entities.Player;
import org.joml.Vector3f;

import static main.Configuration.*;
import static main.entities.Player.currentSpeed;
import static main.entities.Player.currentTurnAngle;
import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component {
    private KeyListener keyboard;
    private Player player;

    public KeyControls(Player player){
        this.player = player;
        this.keyboard = KeyListener.getInstance();
    }

    @Override
    public void update(float deltaTime) {
        float val = 0.02f;

        if(keyboard.isKeyPressed(GLFW_KEY_1)){
            player.increaseRotation(new Vector3f(1, 0, 0));
        }else if(keyboard.isKeyPressed(GLFW_KEY_2)){
            player.increaseRotation(new Vector3f(0, 1, 0));
        }else if(keyboard.isKeyPressed(GLFW_KEY_3)){
            player.increaseRotation(new Vector3f(0, 0, 1));
        }

        if(keyboard.isKeyPressed(GLFW_KEY_UP)){
            currentSpeed = runSpeed;
        }else if(keyboard.isKeyPressed(GLFW_KEY_DOWN)){
            currentSpeed = -runSpeed;
        }else{
            currentSpeed = 0;
        }

        if(keyboard.isKeyPressed(GLFW_KEY_RIGHT)){
            currentTurnAngle = turnAngle;
        }else if(keyboard.isKeyPressed(GLFW_KEY_LEFT)){
            currentTurnAngle = -turnAngle;
        }else{
            currentTurnAngle = 0;
        }

        if(keyboard.isKeyPressed(GLFW_KEY_W)){
            player.increasePosition(new Vector3f(0, 0, val));
        }else if(keyboard.isKeyPressed(GLFW_KEY_S)){
            player.increasePosition(new Vector3f(0, 0, -val));
        }

        if(keyboard.isKeyPressed(GLFW_KEY_SPACE)){
            System.out.println("jump");
            player.jump();
        }

    }
}
