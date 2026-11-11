// Wet Ground for Sodium 0.9 chunk terrain. SodiumTerrain inserts this ahead of Sodium's own
// fragment main(), renamed larp_sodium_main(), and routes Sodium's unfogged color through
// larp_wet(). It is never compiled on its own.
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

#moj_import <larp-client:wet_ground.glsl>

in vec3 larp_position;
in float larp_sky;
float larp_mirror = 0.0;

vec4 larp_wet(vec4 color) {
    larp_mirror = wetSurface(color, larp_position, larp_position + CameraWorld.xyz, larp_sky, v_Color,
        u_FogColor.rgb, u_RenderFog, v_FragDistance.x);
    return color;
}

void larp_sodium_main();

void main() {
    larp_sodium_main();
    if (Modes.y > .5 && larp_mirror > .02) fragColor.a = .25 + .2 * (1.0 - clamp(larp_mirror, 0.0, 1.0));
}
