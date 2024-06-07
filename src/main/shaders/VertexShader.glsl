#version 330 core

in vec3 position;
in vec2 vTexCords;

out vec2 fTexCords;

uniform mat4 transMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projectionMatrix * viewMatrix * transMatrix * vec4(position, 1);
    fTexCords = vTexCords;
}
