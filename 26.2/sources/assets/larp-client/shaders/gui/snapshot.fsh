#version 330

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;
in vec2 localPosition;
flat in vec2 halfSize;
flat in float radius;

out vec4 fragColor;

void main() {
    // The copied frame is opaque; only the rounded window outline, kept where the piece came from,
    // and the piece's own fade make it translucent.
    float r = max(radius, 0.5);
    vec2 q = abs(localPosition) - halfSize + r;
    float distance = min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;
    float footprint = max(fwidth(distance), 0.00001);
    float coverage = clamp(0.5 - distance / footprint, 0.0, 1.0);
    vec4 color = vec4(texture(Sampler0, texCoord0).rgb, 1.0) * vertexColor * ColorModulator;
    color.a *= coverage;
    if (color.a == 0.0) discard;
    fragColor = color;
}
