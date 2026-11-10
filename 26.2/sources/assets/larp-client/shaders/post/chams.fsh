#version 330
uniform sampler2D Surface;
uniform sampler2D ModelDepth;
uniform sampler2D SceneDepth;
layout(std140) uniform ChamsColors { vec4 VisibleColor; vec4 HiddenColor; };
in vec2 texCoord;
out vec4 fragColor;
void main() {
    float coverage = texture(Surface, texCoord).a;
    if (coverage <= 0.0) discard;
    float model = texture(ModelDepth, texCoord).r;
    float scene = texture(SceneDepth, texCoord).r;
    // Both comparisons use the nearest model surface. Back faces cannot masquerade as walls.
    float stableDepth = min(1.0, uintBitsToFloat(floatBitsToUint(model) + 8u));
    vec4 color = stableDepth >= scene ? VisibleColor : HiddenColor;
    fragColor = vec4(color.rgb, color.a * coverage);
}
