#version 330
// Adapted from MoonDLC 1.0.0 CC0 shader; original field math retained.
in vec3 skyDirection;
layout(std140) uniform SkyPalette {
    vec4 LarpTimeResolution;
    vec4 LarpColorAlpha;
    vec4 LarpMotion;
    vec4 LarpCamera;
    vec4 LarpUnused0; vec4 LarpUnused1; vec4 LarpUnused2;
    vec4 LarpUnused3; vec4 LarpUnused4; vec4 LarpUnused5;
};
#define uTime LarpTimeResolution.x
#define uResolution LarpTimeResolution.yz
#define uColor LarpColorAlpha.rgb
#define uAlpha LarpColorAlpha.a
#define uSpeed LarpMotion.x
#define uScale LarpMotion.y
#define uIntensity LarpMotion.z
#define uCameraDir LarpCamera.xy
#define uFov LarpCamera.z



out vec4 fragColor;

const mat2 ROT = mat2(0.80, 0.60, -0.60, 0.80);

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c);
}
mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(c, 0.0, s, 0.0, 1.0, 0.0, -s, 0.0, c);
}

vec3 hsv2rgb(vec3 c) {
    vec3 rgb = clamp(abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

vec3 nmzHash33(vec3 q) {
    uvec3 p = uvec3(ivec3(q));
    p = p * uvec3(374761393U, 1103515245U, 668265263U) + p.zxy + p.yzx;
    p = p.yzx * (p.zxy ^ (p >> 3U));
    return vec3(p ^ (p >> 16U)) * (1.0 / vec3(0xffffffffU));
}

float hash21(vec2 p) {
    uvec2 q = uvec2(ivec2(p)) * uvec2(1597334673U, 3812015801U);
    uint n = (q.x ^ q.y) * 1597334673U;
    return float(n) * (1.0 / 4294967295.0);
}

float vnoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float amp = 0.5;
    float sum = 0.0;
    for (int i = 0; i < 4; i++) {
        sum += amp * vnoise(p);
        p = ROT * p * 2.03;
        amp *= 0.5;
    }
    return sum;
}

vec3 starField(vec3 dir, float time, float density, float twinkle) {
    vec3 c = vec3(0.0);
    vec3 p = dir * 42.0;
    float dens = density;
    float amp = 1.0;
    for (int i = 0; i < 4; i++) {
        vec3 id = floor(p);
        vec3 q = fract(p) - 0.5;
        vec3 rn = nmzHash33(id);
        float core = smoothstep(0.36, 0.0, length(q));
        float hit = step(rn.x, dens);
        float tw = 1.0 - twinkle + twinkle * (0.55 + 0.45 * sin(time * (1.2 + rn.z * 2.6) + rn.y * 47.0));
        vec3 tint = mix(vec3(1.0, 0.72, 0.45), vec3(0.72, 0.86, 1.0), rn.y);
        c += hit * core * core * tint * (0.35 + 0.65 * rn.z) * tw * amp * 0.75;
        p = p * 1.63 + 17.0;
        dens *= 0.62;
        amp *= 0.78;
    }
    return c;
}

void main() {
    float time = uTime * uSpeed;
    vec2 sp = (gl_FragCoord.xy / uResolution.xy) * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;

    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(-sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 rd = rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV;

    float axis = atan(rd.z, rd.x);

    vec3 col = vec3(0.024, 0.036, 0.076) * (0.55 + 0.45 * smoothstep(-1.0, 1.0, rd.y));
    col += starField(rd, time, 0.030, 0.35) * 0.9;

    vec3 aur = vec3(0.0);
    float rawLum = 0.0;

    for (int i = 0; i < 3; i++) {
        float fi = float(i);
        float height = 1.0 + fi * 0.75;
        float seed = fi * 4.7;

        float tp = height / (max(rd.y, 0.0) + 0.13);
        vec2 p = rd.xz * tp * 0.55 * (uScale * 0.2);
        p += vec2(time * 0.045 + seed * 3.3, time * 0.017 - seed * 2.1);

        float d = fbm(p * vec2(0.85, 0.30) + vec2(seed, 0.0));
        float center = 0.46 + fi * 0.075;
        float band = exp(-pow((d - center) / 0.052, 2.0));

        float rays = 0.5 + 0.5 * sin(p.x * 6.0 + fbm(p * 0.6) * 11.0 + time * (0.25 + fi * 0.1));
        band *= 0.30 + 0.70 * rays * rays;

        float fade = smoothstep(0.02 + fi * 0.02, 0.34 + fi * 0.12, rd.y);
        band *= fade;

        rawLum += band * (1.0 - fi * 0.3);

        vec3 tint = mix(uColor, vec3(0.1, 0.9, 0.6), fi * 0.35);
        aur += tint * band * (1.0 - fi * 0.18);
    }

    col += aur * (1.25 * uIntensity * 100.0);

    vec3 horizonTint = uColor;
    float glow = exp(-abs(rd.y) * 6.5) * (0.08 + 0.42 * clamp(rawLum, 0.0, 1.5));
    col += horizonTint * glow * 0.55 * (uIntensity * 100.0);

    fragColor = vec4(col, uAlpha);
}
