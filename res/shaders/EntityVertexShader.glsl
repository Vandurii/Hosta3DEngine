#version 400
//Settings
float density = 0.003;//0.003;
float gradient = 4;//4;

// from vbo1
in vec3 vPos;
in vec2 vTexCords;
in vec3 vNormals;

// Uploaded values.
uniform mat4 transformation;
uniform mat4 view;
uniform mat4 projection;

uniform vec3 lightPos[4];

uniform float fakeLightning;

uniform vec2 texOffset;
uniform int numberOfRows;

// out
out vec3 fPos;
out vec2 fTexCords;

out vec3 fNormals;
out vec3 toLightVector[4];
out vec3 toCamVector;

out float visibility;

void main() {
    // Calculate pixel position.
    vec4 worldPosition = transformation * vec4(vPos, 1);
    vec4 positionRelativeToCamera = view * worldPosition;
    gl_Position = projection * positionRelativeToCamera;

    // Check fake lightning.
    vec3 normals = vNormals;
    if (fakeLightning > 0.5) {
        normals = vec3(1, 1, 1);
    }

    // out
    fPos = vPos;
    fTexCords =  (vTexCords / numberOfRows) + texOffset;

        // Calculate to ligth vector.
    fNormals = (transformation * vec4(normals, 0)).xyz;

    for(int i = 0; i < 4; i++) {
        toLightVector[i] = lightPos[i] - worldPosition.xyz;
    }

        // Calculate to camera vector.
    toCamVector = (inverse (view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;

        // Caculate fog density.
    float distance = length(positionRelativeToCamera);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0, 1);
}