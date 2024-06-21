#version 330 core

// from vbo
in vec3 vPos;

// out
out vec3 fTexCords;

// Uploaded values.
uniform mat4 projection;
uniform mat4 view;

void main(){
  // Calculate pixel position.
    gl_Position = projection * view * vec4(vPos, 1);

    // out
    fTexCords = vPos;
}

