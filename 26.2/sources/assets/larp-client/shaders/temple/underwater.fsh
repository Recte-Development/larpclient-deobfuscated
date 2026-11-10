#version 330
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
uniform sampler2D SceneColor;
uniform sampler2D SceneDepth;
vec3 positionAt(vec2 uv, float depth) {
    float z = WaterFlags.w > .5 ? depth : depth * 2.0 - 1.0;
    vec4 p = InverseViewProjection * vec4(uv * 2.0 - 1.0, z, 1);
    return p.xyz / max(abs(p.w), 1e-7) * sign(p.w);
}
vec3 reflectedWorld(vec3 origin, vec3 direction, out float confidence) {
    confidence = 0.0;
    vec3 result = vec3(0);
    float previous = .08;
    for (int step = 0; step < 32; step++) {
        float travel = .12 + float(step) * .22 + float(step * step) * .12;
        vec3 p = origin + direction * travel;
        vec4 clip = ViewProjection * vec4(p, 1);
        if (clip.w <= .01) break;
        vec3 ndc = clip.xyz / clip.w;
        vec2 uv = ndc.xy * .5 + .5;
        if (any(lessThan(uv, vec2(.001))) || any(greaterThan(uv, vec2(.999)))) break;
        float sceneDepth = texture(SceneDepth, uv).r;
        float rayDepth = WaterFlags.w > .5 ? ndc.z : ndc.z * .5 + .5;
        // Minecraft uses reversed depth, cleared to zero.
        if (sceneDepth > 0.0 && rayDepth < sceneDepth) {
            float low = previous, high = travel;
            for (int refine = 0; refine < 4; refine++) {
                float middle = (low + high) * .5;
                vec4 c = ViewProjection * vec4(origin + direction * middle, 1);
                vec3 n = c.xyz / c.w;
                vec2 s = n.xy * .5 + .5;
                float d = texture(SceneDepth, s).r;
                float r = WaterFlags.w > .5 ? n.z : n.z * .5 + .5;
                if (d > 0.0 && r < d) { high = middle; uv = s; sceneDepth = d; }
                else low = middle;
            }
            float separation = length(origin + direction * high) - length(positionAt(uv, sceneDepth));
            if (separation > clamp((travel - previous) * 1.6, .4, 4.0)) { previous = travel; continue; }
            vec2 edge = min(uv, 1.0 - uv);
            confidence = smoothstep(0.0, .08, min(edge.x, edge.y)) * (1.0 - smoothstep(70.0, 140.0, travel));
            result = texture(SceneColor, uv).rgb;
            break;
        }
        previous = travel;
    }
    return result;
}
float randomCell(vec2 cell) {
    return fract(sin(dot(cell, vec2(127.1, 311.7))) * 43758.5453);
}
float noiseField(vec2 p) {
    vec2 cell = floor(p), q = fract(p);
    q = q * q * (3.0 - 2.0 * q);
    return mix(mix(randomCell(cell), randomCell(cell + vec2(1, 0)), q.x),
               mix(randomCell(cell + vec2(0, 1)), randomCell(cell + vec2(1, 1)), q.x), q.y);
}
float pools(vec2 p) {
    return noiseField(p) * .55 + noiseField(p * 2.13 + 7.1) * .3 + noiseField(p * 4.7 + 3.3) * .15;
}
vec2 rainNormal(vec2 p, float seconds) {
    vec2 result = vec2(0);
    for (int layer = 0; layer < 2; layer++) {
        vec2 q = p * (layer == 0 ? 2.2 : 3.4) + float(layer) * 17.3;
        vec2 cell = floor(q);
        for (int y = -1; y <= 1; y++) for (int x = -1; x <= 1; x++) {
            vec2 id = cell + vec2(x, y);
            vec2 seed = vec2(randomCell(id), randomCell(id + 19.31));
            float age = fract(seconds * (.75 + seed.y * .3) + seed.x * 7.13);
            vec2 offset = q - id - seed;
            float radius = length(offset);
            float envelope = (radius - age * .85) * 16.0;
            result += offset / max(radius, .001) * sin(envelope * 3.141593)
                * exp(-envelope * envelope * .5) * (1.0-age) * (1.0-age);
        }
    }
    return result * .22;
}

in vec2 texCoord;
#define uv texCoord
out vec4 fragColor;
void main() {
    vec3 original=texture(SceneColor,uv).rgb;
    float depth=texture(SceneDepth,uv).r;
    vec3 position=positionAt(uv,max(depth,1e-5));
    float distance=min(length(position),48.0);
    vec3 attenuation=exp(-vec3(.09,.025,.016)*distance);
    vec3 tint=vec3(.025,.11,.14);
    vec3 result=original*attenuation+tint*(1.0-attenuation);
    if (WaterFlags.y>.5 && depth>0.0) {
        vec2 p=(position.xz+Camera.xz)*1.2;
        float light=pow(1.0-min(abs(sin(p.x*3.7+sin(p.y*2.9+WindTime.w)*.8)),abs(sin(p.y*4.3+cos(p.x*3.1-WindTime.w*.8)*.8))),9.0);
        result+=original*attenuation*light*Sun.w*.25;
    }
    vec3 ray=normalize(position);
    float shaft=pow(max(dot(ray,normalize(Sun.xyz)),0.0),10.0);
    float bands=.65+.35*sin(atan(ray.x,ray.z)*48.0+WindTime.w*.25);
    result+=vec3(.14,.23,.2)*shaft*bands*(1.0-exp(-distance*.09))*Sun.w;
    fragColor=vec4(mix(original,result,Water.x),1);
}
