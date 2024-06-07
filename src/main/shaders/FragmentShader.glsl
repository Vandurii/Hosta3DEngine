#version 330 core // 400

in vec2 fTexCords;

out vec4 outColor;

uniform sampler2D textureSampler;

void main(){
    outColor = texture(textureSampler, fTexCords);
}


