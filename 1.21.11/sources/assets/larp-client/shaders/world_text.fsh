#version 330
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
uniform sampler2D Sampler0;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
out vec4 fragColor;
void main() {
#ifdef IS_GRAYSCALE
    float coverage = texture(Sampler0, texCoord0).r;
    vec4 texColor = vec4(1.0, 1.0, 1.0, coverage);
#else
    vec4 texColor = texture(Sampler0, texCoord0);
#endif
    vec4 color = texColor * vertexColor * ColorModulator;
    // No depth writes: preserve low-alpha coverage throughout the spawn/fade envelope.
    if (color.a < 0.001) discard;
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance,
        FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
