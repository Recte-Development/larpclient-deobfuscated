#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <larp-client:sample_lightmap.glsl>
#moj_import <larp-client:single_color.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    sphericalVertexDistance = fog_spherical_distance(pos);
    cylindricalVertexDistance = fog_cylindrical_distance(pos);
    // Terrain material tags ride in vertex alpha codes 2 to 254; they decode to opaque.
    float alpha = Color.a;
    int code = int(round(Color.a * 255.0));
    if (code >= 2 && code <= 254) alpha = 1.0;
    vertexColor = vec4(larp_single_color(Color.rgb) * sample_lightmap(Sampler2, UV2).rgb, alpha);
    texCoord0 = UV0;
}
