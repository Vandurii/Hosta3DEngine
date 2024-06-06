#version 330 core

in vec3 position;

out vec3 color;

void main(){
    color = vec3(position.x + 0.5, 1, position.y + 0.5);
    gl_Position = vec4(position, 1);
}
