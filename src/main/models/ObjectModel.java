package main.models;

public class ObjectModel {
    private RawModel rawModel;
    private TextureModel modelTexture;

    public ObjectModel(RawModel rawModel, TextureModel modelTexture){
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getModelTexture() {
        return modelTexture;
    }
}