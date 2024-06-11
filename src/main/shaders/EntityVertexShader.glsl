#version 400

in vec3 vPos;
in vec2 vTexCords;
in vec3 vNormals;

out vec2 fTexCords;
out vec3 fNormals;
out vec3 toLightVector;
out vec3 toCameraVector;

// Fog
out float visibility;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

uniform vec3 lightPos;
uniform float fakeLightning;

void main(){
    vec3 vAcutalNormals = vNormals;
    if(fakeLightning > 0.5){
        vAcutalNormals = vec3(1.0, 1.0, 1.0);
    }

    // projection, transform, view
    vec4 worldPosition = transformation * vec4(vPos, 1.0);
    vec4 positionRelativeToCam = view * worldPosition;
    gl_Position = projection * positionRelativeToCam;

    fTexCords = vTexCords ;
    fNormals = (transformation * vec4(vAcutalNormals, 0.0)).xyz;
    toLightVector = lightPos - worldPosition.xyz;

    toCameraVector = (inverse (view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    // Fog
    float density = 0.001;
    float gradient = 1.5;
    float d = length(positionRelativeToCam);

    visibility = exp(-pow((d * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);


}
