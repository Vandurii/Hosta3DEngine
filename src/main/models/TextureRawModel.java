package main.models;

import main.textures.TextureModel;

public class TextureRawModel {
    private RawModel rawModel;
    private TextureModel modelTexture;

    public TextureRawModel(RawModel rawModel, TextureModel modelTexture){
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