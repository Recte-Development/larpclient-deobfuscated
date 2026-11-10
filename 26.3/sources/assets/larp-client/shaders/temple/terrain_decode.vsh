#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:terrainglobals.glsl>
#ifndef MULTIDRAW_TERRAIN
#moj_import <minecraft:chunksection.glsl>
#endif
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:sample_lightmap.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
#ifdef MULTIDRAW_TERRAIN
in ivec3 ChunkPosition;
in float ChunkVisibility;
#endif

uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out float chunkVisibility;

void main() {
    vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    sphericalVertexDistance = fog_spherical_distance(pos);
    cylindricalVertexDistance = fog_cylindrical_distance(pos);
    vec4 color = Color;
    int code = int(round(Color.a * 255.0));
    if (code >= 2 && code <= 254) color.a = 1.0;
    vertexColor = color * sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;
    chunkVisibility = mix(1.0, ChunkVisibility, clamp((length(pos) - 16.0) / 16.0, 0.0, 1.0));
}
