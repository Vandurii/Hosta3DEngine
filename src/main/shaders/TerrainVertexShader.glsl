#version 330 core

in vec3 vPos;
in vec2 vTexCords;
in vec3 vNormals;

out vec2 fTexCords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

// Fog
out float visibility;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

uniform vec3 lightPos;

void main(){
    // projection, transform, view
    vec4 worldPosition = transformation * vec4(vPos, 1.0);
    vec4 positionRelativeToCam = view * worldPosition;
    gl_Position = projection * positionRelativeToCam;

    fTexCords = vTexCords  * 40;

    surfaceNormal = (transformation * vec4(vNormals, 0)).xyz;
    toLightVector = lightPos - worldPosition.xyz;

    toCameraVector = (inverse (view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;

    // Fog
    float density = 0.015;
    float gradient = 1.0;
    float d = length(positionRelativeToCam);

    visibility = exp(-pow((d * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);

}
