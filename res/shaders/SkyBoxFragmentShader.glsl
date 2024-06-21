#version 400
// Settings
float loverLimit = 0;
float upperLimit = 30;

// from vertex
in vec3 fTexCords;

// out
out vec4 outCol;

// Uploaded valeus.
uniform samplerCube fCub;
uniform samplerCube sCub;
uniform vec4 fogColor;
uniform float blendFactor;

void main(){
    // mix day and night texture
    vec4 fTexCol = texture(fCub, fTexCords);
    vec4 sTexCol = texture(sCub, fTexCords);
    vec4 finalCol = mix(fTexCol, sTexCol, blendFactor);

    // blure betwen ground and sky
    float factor = (fTexCords.y - loverLimit) / (upperLimit - loverLimit);
    factor = clamp(factor, 0.0, 1.0);

    // Out color.
    outCol = mix(fogColor, finalCol, factor);
}