#version 330

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

// Texels across both sides of an edge; HudSdfAtlasGenerator writes the same distanceRange.
#define DISTANCE_RANGE 8.0

void main() {
    // Alpha holds signed distance to the outline, positive inside, 0.5 on the edge.
    float distance = (texture(Sampler0, texCoord0).a - 0.5) * DISTANCE_RANGE;
    // Atlas texels per framebuffer pixel keeps a one-pixel edge at every HUD scale and rotation.
    vec2 texels = fwidth(texCoord0 * vec2(textureSize(Sampler0, 0)));
    float footprint = max(0.5 * (texels.x + texels.y), 0.0001);
    float coverage = clamp(distance / footprint + 0.5, 0.0, 1.0);
    vec4 color = vertexColor * ColorModulator;
    color.a *= coverage;
    if (color.a == 0.0) discard;
    fragColor = color;
}
