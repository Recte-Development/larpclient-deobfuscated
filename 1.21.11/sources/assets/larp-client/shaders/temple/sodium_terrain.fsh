// Wet Ground and Snow Ground for Sodium 0.8 chunk terrain. SodiumTerrain resolves its Larp
// imports, inserts this ahead of Sodium's own fragment main(), renamed larp_sodium_main(), and
// routes Sodium's unfogged color through larp_terrain(). It is never compiled on its own.
layout(std140) uniform LarpMaterial {
    vec4 WindTime;
    vec4 Water;
    vec4 WaterFlags;
    vec4 Wet;
    vec4 Modes;
    vec4 Sun;
    vec4 Sky;
    vec4 Camera;
    vec4 Snow;
    mat4 ViewProjection;
    mat4 InverseViewProjection;
    vec4 SnowTrailInfo;
    vec4 SnowTrailBounds;
    vec4 SnowTracks[48];
    vec4 CameraWorld;
};

#moj_import <larp-client:snow.glsl>
#moj_import <larp-client:wet_ground.glsl>

in vec3 larp_position;
in vec3 larp_base;
in float larp_sky;
in vec3 larp_light;
in float larp_snow_cap;
in float larp_snow_eligible;
float larp_mirror = 0.0;

vec4 larp_terrain(vec4 color) {
    vec3 world = larp_position + CameraWorld.xyz;
    // Sodium's cylindrical fog distance, rebuilt here because v_FragDistance is written only with fog.
    float cylindrical = max(length(larp_position.xz), abs(larp_position.y));
    larp_mirror = wetSurface(color, larp_position, world, larp_sky, v_Color,
        u_FogColor.rgb, u_RenderFog, cylindrical);
    // Snow covers the wet mirror mask, as in the vanilla terrain material.
    vec3 face = normalize(cross(dFdx(larp_position), dFdy(larp_position)));
    if (dot(face, larp_position) > 0.0) face = -face;
    float footprint = max(length(dFdx(larp_position.xz)), length(dFdy(larp_position.xz)));
    larp_mirror *= 1.0 - snowSurface(color, larp_position, larp_base, world, larp_sky,
        larp_snow_cap, larp_snow_eligible, larp_light, face, footprint);
    return color;
}

void larp_sodium_main();

void main() {
    larp_sodium_main();
    if (Modes.y > .5 && larp_mirror > .02) fragColor.a = .25 + .2 * (1.0 - clamp(larp_mirror, 0.0, 1.0));
}
