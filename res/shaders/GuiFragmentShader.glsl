#version 400
// from vertex
in vec2 fTexCords;

// out
out vec4 outCol;

uniform sampler2D sam;

void main(){
    // Out color.
    outCol = texture(sam, fTexCords);
}