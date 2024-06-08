package main.controller;

import main.entities.Camera;
import main.entities.Direction;

import java.util.PrimitiveIterator;

import static main.entities.Direction.*;
import static org.lwjgl.glfw.GLFW.*;

public class KeyController {

    private KeyListener keyboard;

    public KeyController(){
        this.keyboard = KeyListener.getInstance();
    }

    public void update(){
        if(keyboard.isKeyPressed(GLFW_KEY_UP)){
            Camera.move(Up);
        }else if(keyboard.isKeyPressed(GLFW_KEY_DOWN)){
            Camera.move(Down);
        }else if(keyboard.isKeyPressed(GLFW_KEY_RIGHT)){
            Camera.move(Right);
        }else if(keyboard.isKeyPressed(GLFW_KEY_LEFT)){
            Camera.move(Left);
        }else if(keyboard.isKeyPressed(GLFW_KEY_W)){
            Camera.move(forward);
        }else if(keyboard.isKeyPressed(GLFW_KEY_S)){
            Camera.move(backward);
        }
    }
}
