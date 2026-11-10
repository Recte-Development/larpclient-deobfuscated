// Single Color Blocks: one chosen color, shaded by the block vertex color. The vertex color carries
// Minecraft's face shade, ambient occlusion and any biome tint; the lightmap is applied separately.
layout(std140) uniform LarpSingleColor {
    vec4 BlockColor;
};

vec3 larp_single_color(vec3 vertex) {
    float high = max(vertex.r, max(vertex.g, vertex.b));
    float low = min(vertex.r, min(vertex.g, vertex.b));
    // Untinted vertices are gray, so their brightest channel is the face shade itself. A biome tint
    // (grass, leaves, vines) also darkens it; lift tinted vertices toward the untinted shade.
    float shade = high - low > 0.04 ? min(1.0, high * 1.25) : high;
    return BlockColor.rgb * shade;
}
