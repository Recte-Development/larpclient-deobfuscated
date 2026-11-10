#version 330 core
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat; vec4 ColorModulator; vec3 ModelOffset; mat4 TextureMat;
};
layout(std140) uniform Projection { mat4 ProjMat; };
in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
out vec2 vUV;
out vec4 vColor;
out vec2 vAux;
void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vUV = UV0; vColor = Color;
    // ENTITY's integer pairs preserve complete float payloads without quantization.
    vAux = vec2(uintBitsToFloat((uint(UV1.x) & 65535u) | ((uint(UV1.y) & 65535u) << 16)),
                uintBitsToFloat((uint(UV2.x) & 65535u) | ((uint(UV2.y) & 65535u) << 16)));
}
