package main.components;

import main.entities.EngineObject;

public abstract class Component {
    private EngineObject parent;

    public abstract void update(float deltaTime);

    public EngineObject getParent() {
        return parent;
    }

    public void setParent(EngineObject parent) {
        this.parent = parent;
    }
}
