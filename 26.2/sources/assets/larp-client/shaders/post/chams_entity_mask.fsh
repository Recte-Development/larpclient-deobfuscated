#version 330
uniform sampler2D Source;
uniform sampler2D ModelDepth;
uniform sampler2D SceneDepth;
layout(std140) uniform ChamsParameters {
    vec4 Shape; vec4 Motion; vec4 Color1; vec4 Color2; vec4 Direction;
};
in vec2 texCoord;
out vec4 fragColor;
void main() {
    vec4 surface = texture(Source, texCoord);
    float model = texture(ModelDepth, texCoord).r;
    float scene = texture(SceneDepth, texCoord).r;
    // Resolve wall visibility only after all cutout layers select their nearest surface.
    // The shared reversed-depth bias keeps native skin and this overlay coplanar.
    float stableDepth = min(1.0, uintBitsToFloat(floatBitsToUint(model) + 8u));
    fragColor = Direction.z > 0.5 || stableDepth >= scene ? surface : vec4(0.0);
}
