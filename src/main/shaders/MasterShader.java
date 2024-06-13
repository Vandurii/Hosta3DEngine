package main.shaders;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL40.glUniformMatrix4dv;

public abstract class MasterShader {
    private int vertexShaderID;
    private int fragmentShaderID;
    private int shaderProgramID;

    public MasterShader(String vertexSource, String fragmentSource){
        // Create shader program.
        vertexShaderID = createShader(vertexSource, GL_VERTEX_SHADER);
        fragmentShaderID = createShader(fragmentSource, GL_FRAGMENT_SHADER);
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(shaderProgramID);
        glValidateProgram(shaderProgramID);
    }

    public abstract void bindAttributes();

    public void bindAttribute(int index, String name){
        glBindAttribLocation(shaderProgramID, index, name);
    }

    public int createShader(String source, int type){
        String shaderSource = "";

        // Get Shader content from file, print message and exit program if that can't be done.
        try{
            shaderSource = new String(Files.readAllBytes(Path.of(source)));
        }catch (Exception e){
            System.out.println("Can't read file: " + source);
            e.printStackTrace();
            System.exit(-1);
        }

        // Create shader.
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        // Print error info and exit program if compile process fail.
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.out.println(source);
            System.exit(-1);
        }

        return shaderID;
    }

    public void start(){
        glUseProgram(shaderProgramID);
    }

    public void stop(){
        glUseProgram(0);
    }

    public <T> void uploadValue(String varName, T type){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        if(type instanceof Matrix4f) {
            FloatBuffer matBuff = BufferUtils.createFloatBuffer(16);
            ((Matrix4f)type).get(matBuff);
            glUniformMatrix4fv(varLocation, false, matBuff);
        }else if (type instanceof Matrix3f){
            FloatBuffer matBuff = BufferUtils.createFloatBuffer(9);
            ((Matrix3f)type).get(matBuff);
            glUniformMatrix3fv(varLocation, false, matBuff);
        }else if(type instanceof Matrix4d){
            DoubleBuffer matBuff = BufferUtils.createDoubleBuffer(16);
            ((Matrix4d)type).get(matBuff);
            glUniformMatrix4dv(varLocation, false, matBuff);
        }else if(type instanceof Vector4f value){
            glUniform4f(varLocation, value.x, value.y, value.z, value.w);
        }else if(type instanceof Vector3f value){
            glUniform3f(varLocation, value.x, value.y, value.z);
        }else if(type instanceof Vector2f value){
            glUniform2f(varLocation, value.x, value.y);
        }else if(type instanceof Float value){
            glUniform1f(varLocation, value);
        }else if(type instanceof Integer value){
            glUniform1i(varLocation, value);
        }else if(type instanceof int[]){
            int[] value = (int[]) type;
            glUniform1iv(varLocation, value);
        }else if(type instanceof Boolean value){
            float val = value ? 1 : 0;
            glUniform1f(varLocation, val);
        }else{
            throw new IllegalStateException("Unexpected value in shader class uploadValue method. --> type:" + type.getClass().getSimpleName() );
        }
    }
}
