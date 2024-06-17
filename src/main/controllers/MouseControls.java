package main.controllers;

import main.components.Component;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component {
    private MouseListener mouse;

    public MouseControls(){
        this.mouse = MouseListener.getInstance();
    }

    @Override
    public void update(float deltaTime) {
        if(mouse.isButtonPressed(GLFW_MOUSE_BUTTON_1)){
       //     System.out.println("Mouse button pressed: 1");
        }else if(mouse.isButtonPressed(GLFW_MOUSE_BUTTON_2)){
        //    System.out.println("Mouse button pressed: 2");
        }

        if(mouse.isMouseDragging()){
        //    System.out.println("Mouse: dragging");
        }
    }
}
