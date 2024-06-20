#version 330 core

// from vbo
in vec2 vPos;
in vec2 vTexCords;

// out
out vec2 fTexCords;

// Uploaded values.
uniform mat4 transformation;

void main(){
  // Calculate pixel position.
    gl_Position = transformation * vec4(vPos, 0, 1);

    // out
    fTexCords = vec2((vPos.x+1.0)/2.0, 1 - (vPos.y+1.0)/2.0);
}

