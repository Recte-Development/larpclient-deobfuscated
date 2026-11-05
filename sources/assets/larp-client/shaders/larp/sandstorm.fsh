#version 330
// Original Larp sandstorm: a towering dust wall rolling across the desert, a veiled sun, layered
// wind-torn dust sheets and fast grit streaks over dune ridges that smoke at the crest.
in vec3 skyDirection;
layout(std140) uniform SkyPalette {
    vec4 LarpTimeResolution;
    vec4 LarpColorAlpha;
    vec4 LarpMotion;
    vec4 LarpCamera;
    vec4 LarpUnused0; vec4 LarpUnused1; vec4 LarpUnused2;
    vec4 LarpUnused3; vec4 LarpUnused4; vec4 LarpUnused5;
};

out vec4 OutColor;

const float PI = 3.14159265;
const float TAU = 6.28318531;
// Azimuth of the sun and of the dust wall.
const float SUN = 0.9;
const float WALL = -1.9;

float hash11(float p) {
    p = fract(p * 0.1031);
    p *= p + 33.33;
    p *= p + p;
    return fract(p);
}

float hash13(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

float hash12(vec2 p) {
    vec3 q = fract(vec3(p.xyx) * 0.1031);
    q += dot(q, q.yzx + 33.33);
    return fract((q.x + q.y) * q.z);
}

float noise3(vec3 x) {
    vec3 i = floor(x);
    vec3 f = fract(x);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(mix(hash13(i), hash13(i + vec3(1.0, 0.0, 0.0)), f.x),
            mix(hash13(i + vec3(0.0, 1.0, 0.0)), hash13(i + vec3(1.0, 1.0, 0.0)), f.x), f.y),
        mix(mix(hash13(i + vec3(0.0, 0.0, 1.0)), hash13(i + vec3(1.0, 0.0, 1.0)), f.x),
            mix(hash13(i + vec3(0.0, 1.0, 1.0)), hash13(i + vec3(1.0, 1.0, 1.0)), f.x), f.y),
        f.z);
}

const mat3 OCTAVE = mat3(0.00, 0.80, 0.60, -0.80, 0.36, -0.48, -0.60, -0.48, 0.64);

float fbm(vec3 p, int octaves) {
    float sum = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 6; i++) {
        if (i >= octaves) break;
        sum += amplitude * noise3(p);
        p = OCTAVE * p * 2.03;
        amplitude *= 0.5;
    }
    return sum;
}

// The sky tint recolors the storm toward its hue at the same brightness, as far as the tint is
// saturated. White and grey tints (the default theme and custom color) keep the natural ochre.
// The sun's hot core stays near white. LarpSkyHorizon.themed mirrors this for terrain fog.
vec3 themed(vec3 c, vec3 tint) {
    const vec3 LUMA = vec3(0.2126, 0.7152, 0.0722);
    float top = max(tint.r, max(tint.g, tint.b));
    float saturation = (top - min(tint.r, min(tint.g, tint.b))) / max(top, 1e-4);
    vec3 hue = tint / max(dot(tint, LUMA), 0.2);
    float brightness = dot(c, LUMA);
    float amount = 0.85 * saturation * (1.0 - smoothstep(0.9, 1.8, brightness));
    return mix(c, hue * brightness, amount);
}

vec3 direction(float azimuth, float elevation) {
    return vec3(sin(azimuth) * cos(elevation), sin(elevation), cos(azimuth) * cos(elevation));
}

// Dust density on a ring of azimuths that drifts with the wind; seam-free around the circle.
float sheet(vec3 d, float azimuth, float elevation, float t, float speed, float stretch, float seed, int octaves) {
    float a = azimuth - t * speed;
    float ring = fbm(vec3(cos(a) * stretch, sin(a) * stretch, elevation * 7.0 + seed + t * 0.04), octaves);
    // Overhead the ring coordinates pinch to a point, so the dust turns into plain 3D noise there.
    float dome = fbm(d * (stretch * 1.4) + vec3(seed, t * 0.03, -t * speed * 2.0), octaves);
    return mix(ring, dome, smoothstep(0.55, 1.05, elevation));
}

// Grit blown past in thin horizontal streaks, three speeds and depths.
float grit(float azimuth, float elevation, float t, float pixel) {
    float sum = 0.0;
    for (int layer = 0; layer < 3; layer++) {
        float fl = float(layer);
        float columns = floor(mix(40.0, 120.0, fl / 2.0));
        float rows = mix(34.0, 70.0, fl / 2.0);
        float speed = mix(0.7, 2.3, fl / 2.0);
        float a = azimuth - t * speed;
        vec2 p = vec2(a / TAU * columns, elevation * rows);
        vec2 id = vec2(mod(floor(p.x), columns), floor(p.y));
        float h = hash12(id + fl * 19.0);
        if (h > 0.13) continue;
        vec2 f = fract(p);
        float lengthCells = mix(0.35, 0.9, hash12(id + 3.0));
        float x0 = hash12(id + 5.0) * (1.0 - lengthCells);
        float along = clamp((f.x - x0) / lengthCells, 0.0, 1.0);
        float inside = step(x0, f.x) * step(f.x, x0 + lengthCells);
        float y0 = 0.3 + 0.4 * hash12(id + 8.0);
        float thick = max(0.0012, pixel * 0.7) * rows;
        float line = exp(-pow((f.y - y0) / max(thick, 0.02), 2.0)) * inside * pow(along, 0.6);
        float lowBias = mix(1.0, 0.0, smoothstep(0.05, 0.8, elevation));
        sum += line * mix(0.35, 0.8, fl / 2.0) * lowBias;
    }
    return sum;
}

void main() {
    vec3 d = normalize(skyDirection);
    float t = LarpTimeResolution.x;
    vec3 accent = max(LarpColorAlpha.rgb, vec3(0.05));
    float h = clamp(d.y, -1.0, 1.0);
    vec2 horizontal = dot(d.xz, d.xz) < 1e-10 ? vec2(0.0, 1.0) : d.xz;
    float azimuth = atan(horizontal.x, horizontal.y);
    float elevation = asin(h);
    float pixel = 2.0 * tan(radians(LarpCamera.z) * 0.5) / max(LarpTimeResolution.z, 1.0);

    // Ochre light diffused through dust: bright at the horizon, rust-brown overhead.
    vec3 zenith = vec3(0.2, 0.1, 0.065);
    vec3 mid = vec3(0.52, 0.28, 0.12);
    vec3 low = vec3(0.86, 0.54, 0.22);
    vec3 sky = mix(low, mid, smoothstep(0.0, 0.3, h));
    sky = mix(sky, zenith, smoothstep(0.25, 1.0, h));
    sky = mix(sky, sky * (0.75 + accent * 0.5), 0.1);

    // Veiled sun with a wide amber glow.
    vec3 sunDir = direction(SUN, 0.33);
    float sunAngle = acos(clamp(dot(d, sunDir), -1.0, 1.0));
    float veil = smoothstep(0.3, 0.7, fbm(vec3(d.xz * 3.0 + vec2(t * 0.05, 0.0), h * 3.0), 3));
    vec3 sun = vec3(1.0, 0.82, 0.5) * smoothstep(0.03 + pixel, 0.03 - pixel, sunAngle) * 2.2 * (1.0 - 0.75 * veil)
             + vec3(1.0, 0.65, 0.25) * (exp(-sunAngle / 0.09) * 0.8 + exp(-sunAngle / 0.45) * 0.5);
    sky += sun;

    // Wind-torn dust sheets at three depths. Dark where thick, golden where the sun reaches through.
    float sunLight = pow(max(dot(d, sunDir), 0.0), 3.0);
    vec3 dust = vec3(0.0);
    float thickness = 0.0;
    vec3 colorA = vec3(0.3, 0.15, 0.07);
    vec3 colorB = vec3(1.0, 0.68, 0.3);
    for (int layer = 0; layer < 3; layer++) {
        float fl = float(layer);
        float s = sheet(d, azimuth, elevation, t, 0.1 + 0.07 * fl, mix(1.6, 3.2, fl / 2.0), fl * 11.0, 4);
        float cover = smoothstep(0.34, 0.66, s) * (0.65 - 0.12 * fl);
        vec3 tone = mix(colorA, colorB, clamp(sunLight * 0.9 + (s - 0.45) * 1.2 + 0.1 * (1.0 - fl / 2.0), 0.0, 1.0));
        dust = mix(dust, tone * (0.8 - 0.08 * fl), cover);
        thickness = max(thickness, cover);
    }
    sky = mix(sky, dust, clamp(thickness * 0.9 * smoothstep(-0.05, 0.2, h) + 0.15 * smoothstep(0.3, 1.0, h), 0.0, 1.0));

    // The dust wall: a boiling front towering over the horizon with a lit, ragged crest.
    float wa = mod(azimuth - WALL + PI, TAU) - PI;
    vec2 local = vec2(wa * cos(elevation), elevation);
    float span = exp(-wa * wa / 1.3);
    float roll = fbm(vec3(local.x * 3.2, local.y * 5.5 - t * 0.12, t * 0.05), 5);
    float ragged = fbm(vec3(local.x * 9.0, local.y * 12.0 - t * 0.2, 2.0 + t * 0.08), 3);
    float crest = 0.24 + 0.32 * span + 0.2 * (roll - 0.5) + 0.05 * (ragged - 0.5);
    float wall = smoothstep(crest + 0.02, crest - 0.02, elevation) * smoothstep(0.02, 0.5, span + 0.2);
    float bodyRelief = clamp(0.5 + (roll - fbm(vec3(local.x * 3.2 + 0.07, local.y * 5.5 - t * 0.12 - 0.06, t * 0.05), 3)) * 5.0, 0.0, 1.0);
    float edge = smoothstep(0.1, 0.0, crest - elevation);
    vec3 wallColor = mix(vec3(0.12, 0.06, 0.035), vec3(0.62, 0.32, 0.13), bodyRelief * 0.7 + 0.3 * edge);
    wallColor += vec3(1.0, 0.7, 0.35) * edge * (0.35 + 0.65 * max(dot(vec2(cos(WALL - SUN), 0.0), vec2(1.0, 0.0)), 0.0)) * 0.35;
    sky = mix(sky, wallColor, wall * 0.93);

    vec3 color = sky;

    // Dunes: a far golden ridge and a near shadowed one, smoking at the crest as the wind lifts sand.
    float farRidge = 0.012 + 0.04 * fbm(vec3(cos(azimuth) * 2.0, sin(azimuth) * 2.0, 3.0), 4);
    float nearRidge = 0.0 + 0.028 * fbm(vec3(cos(azimuth) * 3.4, sin(azimuth) * 3.4, 9.0), 4);
    vec3 haze = vec3(0.66, 0.4, 0.18);
    color = mix(color, mix(vec3(0.4, 0.2, 0.09), haze, 0.5), smoothstep(pixel, -pixel, elevation - farRidge));
    float plume = exp(-max(elevation - nearRidge, 0.0) / 0.02) * smoothstep(-0.01, 0.0, elevation - nearRidge + 0.01);
    float streak = smoothstep(0.35, 0.8, fbm(vec3(azimuth * 18.0 - t * 1.5, elevation * 40.0, t * 0.2), 3));
    color += vec3(1.0, 0.7, 0.35) * plume * streak * 0.35;
    color = mix(color, vec3(0.24, 0.12, 0.055) + vec3(0.2, 0.1, 0.03) * smoothstep(0.02, 0.0, elevation - nearRidge), smoothstep(pixel, -pixel, elevation - nearRidge));

    color += vec3(1.0, 0.78, 0.45) * grit(azimuth, elevation, t, pixel) * smoothstep(-0.02, 0.01, elevation) * 0.55;

    // Below the horizon everything is dust haze for the fog to blend with.
    color = mix(color, haze * 0.85, smoothstep(-0.01, -0.08, h));

    color = themed(color, accent);
    color = 1.0 - exp(-max(color, vec3(0.0)) * 1.25);
    OutColor = vec4(color, LarpColorAlpha.a);
}
