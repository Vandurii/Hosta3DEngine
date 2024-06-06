package main.renderEngine;

import static main.Configuration.clearColor;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    public void prepare(){
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(clearColor.x, clearColor.y, clearColor.z,1);
        //    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public void render(RawModel rawModel){
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
