package main.renderer;

import main.converter.Loader;
import main.models.GuiTextureModel;
import main.models.RawModel;
import main.shaders.GuiShader;
import main.toolbox.Maths;
import org.joml.Matrix4f;

import java.util.List;

import static main.Configuration.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_SRC0_ALPHA;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GuiRenderer {
    private RawModel quad;
    private GuiShader guiShader;

    public GuiRenderer(){
        float[] position = {
                -1,  1,
                -1, -1,
                 1,  1,
                 1, -1
        };

        this.guiShader = new GuiShader(guiVertexShaderPath, guiFragmentShaderPath);
        this.quad = Loader.getInstance().loadToVao(position, 2);
    }

    public void render(List<GuiTextureModel> guiList){

        guiShader.start();
        glBindVertexArray(quad.getVaoID());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);

        for(GuiTextureModel gui: guiList){
            Matrix4f transform = Maths.transform(gui.getPosition(), gui.getScale());
            guiShader.uploadValue(transformationID, transform);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gui.getID() );
            glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVerticesCount());
        }


        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        guiShader.stop();
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
