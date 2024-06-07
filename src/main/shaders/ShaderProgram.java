package main.shaders;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL40.glUniformMatrix4dv;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String vertexFile, String fragmentFile){
        vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        glBindAttribLocation(programID, attribute, variableName);
    }

    public void start(){
        glUseProgram(programID);
    }

    public int loadShader(String file, int type){
        String shaderSource = "";

        try {
            shaderSource = new String(Files.readAllBytes(Path.of(file)));
        }catch (IOException exception){
            exception.printStackTrace();;
            System.exit(-1);
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.exit(-1);
        }

        return shaderID;
    }

    public <T> void uploadValue(String varName, T type){
        int varLocation = glGetUniformLocation(programID, varName);

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
        }else{
            throw new IllegalStateException("Unexpected value in shader class uploadValue method. --> type:" + type.getClass().getSimpleName() );
        }
    }

    public void stop(){
        glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}
