package main.controller;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static double x, y, scroll;
    private static boolean isMouseDragged;
    private static boolean[] buttonPressed;
    private static MouseListener instance;

    private MouseListener(){
        buttonPressed = new boolean[3];
    }

    public static void cursorPositionCallback(long window, double x, double y){
        if(instance == null) getInstance();
        MouseListener.x = x;
        MouseListener.y = y;

        // If mouse is moving and at least one button is pressed then mouse is dragging.
        for(boolean val: buttonPressed){
            if(val){
                isMouseDragged = true;
                break;
            }
        }
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(instance == null) getInstance();

        if(action == GLFW_PRESS){
            buttonPressed[button] = true;
        }else if(action == GLFW_RELEASE){
            buttonPressed[button] = false;
            // If button is released and no more buttons are pressed then mouse is not dragging, if at least one button is pressed then return.
            for(boolean val: buttonPressed){
                if(val) return;
            }
            isMouseDragged = false;
        }
    }

    public static void scrollCallback(long window, double xOffset, double yOffset){
        if(instance == null) getInstance();

        scroll = (float) yOffset;
    }


    public boolean isButtonPressed(int buttonCode) {
        return buttonPressed[buttonCode];
    }

    public boolean isMouseDragging(){
        return isMouseDragged;
    }

    public double getScroll() {
        double val = scroll;
        scroll = 0;

        return val;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static MouseListener getInstance(){
        if(instance == null) instance = new MouseListener();

        return instance;
    }

    public Vector2d getMouse(){
        return  new Vector2d(getX(), getY());
    }
}
