#version 330 core

in vec3 position;
in vec2 vTexCords;
in vec3 vNormals;

out vec2 fTexCords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPos;

void main(){
    vec4 worldPosition = transMatrix * vec4(position, 1);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    fTexCords = vTexCords  * 40;

    surfaceNormal = (transMatrix * vec4(vNormals, 0)).xyz;
    toLightVector = lightPos - worldPosition.xyz;

    toCameraVector = (inverse (viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
}
