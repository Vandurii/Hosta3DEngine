package main.models;

public class ObjectModel {
    private RawModel rawModel;
    private TextureModel textureModel;

    public ObjectModel(RawModel rawModel, TextureModel textureModel){
        this.rawModel = rawModel;
        this.textureModel = textureModel;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getTextureModel() {
        return textureModel;
    }
}
