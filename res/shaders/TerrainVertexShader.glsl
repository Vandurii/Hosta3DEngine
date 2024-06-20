#version 400

//Settings
float density = 0.003;//0.003;
float gradient = 4;//4;

// from vbo
in vec3 vPos;
in vec2 vTexCords;
in vec3 vNormals;

// out
out vec3 fPos;
out vec2 fTexCords;

out vec3 fNormals;
out vec3 toLightVector[4];
out vec3 toCamVector;

out float visibility;

// Uploaded values.
uniform mat4 transformation;
uniform mat4 view;
uniform mat4 projection;

uniform vec3 lightPos[4];

void main(){
    // Calculate pixel position.
    vec4 worldPosition = transformation * vec4(vPos, 1);
    vec4 positionRelativeToCamera = view * worldPosition;
    gl_Position = projection * positionRelativeToCamera;

    // out
    fPos = vPos;
    fTexCords = vTexCords;

        // Calculate to ligth vector.
    fNormals = (transformation * vec4(vNormals, 0)).xyz;

    for(int i = 0; i < 4; i++) {
        toLightVector[i] = lightPos[i] - worldPosition.xyz;
    }

        // Calculate to camera vector.
    toCamVector = (inverse (view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;

    // Caculate fog density.
    float distancee = length(positionRelativeToCamera);
    visibility = exp(-pow((distancee * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}