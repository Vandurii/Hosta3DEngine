package main.controller;

import main.entities.Player;

import static main.Configuration.runSpeed;
import static main.Configuration.turnSpeed;
import static main.entities.Player.currentSpeed;
import static main.entities.Player.currentTurnSpeed;
import static org.lwjgl.glfw.GLFW.*;

public class KeyController {

    private KeyListener keyboard;

    public KeyController(){
        this.keyboard = KeyListener.getInstance();
    }

    public void update(Player player){
        // run
        if(keyboard.isKeyPressed(GLFW_KEY_W)){
            currentSpeed = runSpeed;
        }else if(keyboard.isKeyPressed(GLFW_KEY_S)){
            currentSpeed = -runSpeed;
        }else{
            currentSpeed = 0;
        }

        // turn out
        if(keyboard.isKeyPressed(GLFW_KEY_A)){
            currentTurnSpeed = turnSpeed;
        }else if(keyboard.isKeyPressed(GLFW_KEY_D)){
            currentTurnSpeed = -turnSpeed;
        }else{
            currentTurnSpeed = 0;
        }

        if(keyboard.isKeyPressed(GLFW_KEY_SPACE)){
            player.jump();
        }
    }
}
