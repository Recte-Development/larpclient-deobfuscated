#version 330
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <larp-client:sample_lightmap.glsl>
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
};

#moj_import <larp-client:snow.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
uniform sampler2D Sampler2;
out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec3 materialPosition;
out vec3 materialWorld;
out vec3 materialBasePosition;
out float materialSkyLight;
out float materialSnowCap;
out float materialSnowEligible;
out vec3 materialLight;
#ifdef LARP_WATER
flat out int waterKind;
flat out vec2 waterFlow;
out vec3 waterTint;
out vec3 waterLight;
#endif

float windEnvelope(vec2 p, float t) {
    float front = .5 + .5 * sin(dot(p, vec2(.021, .013)) - t * .55);
    float swell = .5 + .5 * sin(t * .23 + p.x * .004);
    return mix(1.0, .35 + 1.25 * front * (.6 + .4 * swell), WindTime.z);
}
vec3 foliageMotion(vec3 p, float flutter) {
    float t = WindTime.x;
    float phase = dot(p, vec3(.37, .19, .61));
    float envelope = windEnvelope(p.xz, t);
    float swing = .55 * sin(t * 1.35 + phase) + .45 * sin(t * .77 + phase * .6);
    vec3 offset = vec3(swing * .9 + sin(t * 3.9 + phase * 2.3) * (.12 + .18 * flutter),
                       sin(t * 2.7 + phase * 1.4) * .12 * flutter,
                       swing * .6 + cos(t * 3.3 + phase * 1.9) * (.12 + .18 * flutter));
    offset.xz += vec2(.35, .22) * (envelope - .6);
    return offset * envelope;
}
float waterHeight(vec2 p, float t) {
    return .5 * sin(dot(p, vec2(.38, .21)) + t * 1.1)
        + .3 * sin(dot(p, vec2(-.27, .46)) + t * 1.4)
        + .14 * sin(dot(p, vec2(.71, -.53)) + t * 2.1)
        + .06 * sin(dot(p, vec2(-.93, -.88)) + t * 2.9);
}
void main() {
    vec3 p = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
    materialBasePosition = p;
    vec3 world = Position + vec3(ChunkPosition);
    vec4 color = Color;
    int code = int(round(Color.a * 255.0));
#ifdef LARP_WATER
    waterKind = code == 224 ? 1 : code >= 192 && code < 224 ? 2 : code == 160 ? 3 : code == 150 ? 4 : 0;
    waterFlow = vec2(0);
    if (waterKind != 0) {
        color.a = 1.0;
        if (waterKind == 2) {
            float direction = float(code - 192) * .19634954;
            waterFlow = vec2(cos(direction), sin(direction));
        }
        if (fract(Position.y) > .02 && waterKind < 3) {
            p.y += (waterHeight(p.xz + Camera.xz, WindTime.w) - .5) * .07 * Water.y * Water.x * (1.0 + Sky.a * .6);
        }
    }
    waterTint = color.rgb;
    waterLight = sample_lightmap(Sampler2, UV2).rgb;
#else
    float weight = code == 254 ? .075 : code == 253 ? .05 : code >= 150 && code <= 240 ? float(code-150) * .0026 : 0.0;
    float leaf = code == 254 || code == 253 || code >= 2 && code <= 99 ? 1.0 : 0.0;
    float treeHeight = code >= 100 && code <= 149 ? float(code-100)*.5 : code >= 2 && code <= 99 ? float(code-2)*.25 : -1.0;
    if (treeHeight >= 0.0 && leaf > .5) weight = .075;
    if (code >= 2 && code <= 254) color.a = 1.0;
    if (weight > 0.0) p += foliageMotion(world, leaf) * weight * WindTime.y;
    if (treeHeight >= 0.0) {
        float phase = dot(world.xz, vec2(.045, .031));
        float envelope = windEnvelope(world.xz, WindTime.x);
        float swing = .6 * sin(WindTime.x * .85 + phase) + .4 * sin(WindTime.x * .53 + phase * 1.7);
        vec2 bend = vec2(.9, .6) * swing * .6 + vec2(.35, .22) * (envelope-.4);
        float amount = pow(treeHeight, 1.5) * .0045 * envelope * WindTime.y;
        p += vec3(bend.x*amount, -abs(amount)*.08, bend.y*amount);
    }
#endif
    materialSnowCap = code == 252 || code == 249 ? 1.0 : 0.0;
    materialSnowEligible = code == 249 || code == 251 || code == 252 || code == 254 || code >= 2 && code <= 149 ? 1.0 : 0.0;
#ifndef LARP_WATER
    if (code == 252 && Snow.x > 0.0) {
        float exposed = smoothstep(.92,.99,float(UV2.y)/240.0);
        p.y += snowDisplacement(world,materialBasePosition,exposed,true);
    }
#endif
    materialLight = sample_lightmap(Sampler2, UV2).rgb;
    gl_Position = ProjMat * ModelViewMat * vec4(p, 1);
    sphericalVertexDistance = fog_spherical_distance(p);
    cylindricalVertexDistance = fog_cylindrical_distance(p);
    vertexColor = color * sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;
    materialPosition = p;
    materialWorld = world;
    materialSkyLight = float(UV2.y) / 240.0;
}
