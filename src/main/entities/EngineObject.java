package main.entities;

import main.components.Component;

import java.util.ArrayList;
import java.util.List;

public class EngineObject {
    private String name;
    private List<Component> componenentsList;

    public EngineObject(String name){
        this.name = name;
        this.componenentsList = new ArrayList<>();
    }

    public void update(float dt){
        for(Component component: componenentsList){
            component.update(dt);
        }
    }

    public void addComponent(Component component){
        component.setParent(this);
        componenentsList.add(component);
    }

    public<T> T getComponent(Class<T> component){
        for(Component c: componenentsList){
            if(c.getClass().isAssignableFrom(component)){
                return component.cast(c);
            }
        }

        System.out.println(String.format("Unable to find component: %s in object: %s", component.getSimpleName(), name));
        return null;
    }
}
