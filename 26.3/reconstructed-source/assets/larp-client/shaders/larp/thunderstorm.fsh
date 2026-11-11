#version 330
// Original Larp thunderstorm: layered storm clouds with lightning crawling sideways across them.
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
const int CHANNELS = 4;
const int BRANCHES = 5;
// Dim stepped leader before the return stroke lights the whole channel.
const float LEADER = 0.18;
const float LIFETIME = 1.3;

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

// Rotating each octave hides the axis-aligned lattice of value noise.
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

// Integer lattice hash: neighboring cells must agree bit for bit on their shared vertex. A float
// hash of (i + 1) * k lets the compiler round each side differently and the path visibly jumps.
float lattice(int i, float seed) {
    uint x = uint(i + 1048576) ^ (uint(seed * 97.0) * 0x9E3779B9u);
    x ^= x >> 16; x *= 0x7FEB352Du;
    x ^= x >> 15; x *= 0x846CA68Bu;
    x ^= x >> 16;
    return float(x) * (2.0 / 4294967296.0) - 1.0;
}

// Piecewise-linear zigzag gives the sharp kinks of a real discharge path. Returns value and slope.
vec2 zigzag(float x, float seed) {
    float i = floor(x);
    float a = lattice(int(i), seed);
    float b = lattice(int(i) + 1, seed);
    return vec2(mix(a, b, x - i), b - a);
}

// Fractal sideways offset of a channel (radians) and its derivative along the channel.
vec2 channel(float x, float span, float seed, int octaves) {
    float frequency = 3.2 / span;
    float amplitude = 0.075 * span;
    vec2 sum = vec2(0.0);
    for (int i = 0; i < 6; i++) {
        if (i >= octaves) break;
        vec2 z = zigzag(x * frequency, seed + float(i) * 13.17);
        sum += vec2(z.x * amplitude, z.y * amplitude * frequency);
        frequency *= 2.3;
        amplitude *= 0.47;
    }
    return sum;
}

// Perpendicular distance from the sideways offset. The slope correction only holds near the
// channel; far away it would jump at every kink and streak the glow, so it fades out with distance.
float perpendicular(float raw, float slope, float width) {
    return raw / mix(1.0, sqrt(1.0 + slope * slope), exp(-raw / (width * 1.5)));
}

float bow(float along, float span, float seed) {
    // Long crawlers follow the cloud base in a gentle arc instead of a ruler line.
    return 0.06 * span * sin(PI * clamp(along, 0.0, 1.0)) * (hash11(seed + 2.3) - 0.35);
}

void strike(int index, float azimuth, float elevation, float pixel, float rate, float front,
            vec3 accent, inout vec3 bolt, inout float flash) {
    float fi = float(index);
    float period = max((2.4 + 1.3 * fi) / rate, LIFETIME + 0.2);
    float clock = LarpTimeResolution.x + fi * 0.731 * period;
    float slot = floor(clock / period);
    float seed = fract(slot * 0.1373 + fi * 0.377) * 991.0;
    float kind = hash11(seed);
    float age = clock - slot * period - hash11(seed + 1.7) * (period - LIFETIME);
    // Half the slots strike, some only flash inside the clouds and the rest stay dark.
    if (kind > 0.8 || age < 0.0 || age > LIFETIME) return;

    float pulses = 1.0 + floor(hash11(seed + 8.9) * 3.0);
    float flicker = 0.0;
    for (int k = 0; k < 3; k++) {
        if (float(k) >= pulses) break;
        float onset = LEADER + float(k) * (0.09 + 0.06 * hash11(seed + 9.7 + float(k)));
        if (age >= onset) flicker = max(flicker, exp(-(age - onset) * 11.0) * (1.0 - 0.22 * float(k)));
    }
    float afterglow = age > LEADER ? exp(-(age - LEADER) * 2.6) : 0.0;
    float fade = smoothstep(LIFETIME, LIFETIME - 0.3, age);

    float az0 = hash11(seed + 3.1) * TAU;
    float el0 = mix(0.12, 0.58, hash11(seed + 4.3));
    vec2 local = vec2((mod(azimuth - az0 + PI, TAU) - PI) * cos(el0), elevation - el0);

    if (kind > 0.5) {
        float sheet = exp(-dot(local, local) * 2.2);
        flash += (flicker + 0.2 * afterglow) * (sheet * 1.4 + 0.06) * fade;
        return;
    }

    float span = mix(0.85, 1.9, hash11(seed + 5.9));
    float tilt = (hash11(seed + 6.7) - 0.5) * 0.45;
    float dir = hash11(seed + 7.3) < 0.5 ? -1.0 : 1.0;
    float c = cos(tilt), s = sin(tilt);
    vec2 p = vec2(c * local.x + s * local.y, -s * local.x + c * local.y);
    float along = p.x / span + 0.5;
    float travel = dir > 0.0 ? along : 1.0 - along;
    float lead = clamp(age / LEADER, 0.0, 1.0);
    float energy = (max(flicker, 0.22 * afterglow) + (age < LEADER ? 0.3 : 0.0)) * fade;

    float width = max(0.0024, pixel * 0.85);
    float halo = width * 7.0;

    // Main channel. Overshoot past the ends or the leader front rounds the stroke into a cap,
    // so the halo and bloom never show a hard edge.
    vec2 h = channel(p.x, span, seed, 6);
    float offset = perpendicular(abs(p.y - h.x - bow(along, span, seed)), h.y, width);
    float over = max(max(-along, along - 1.0), travel - lead);
    float dist = length(vec2(max(over, 0.0) * span, offset));
    float taper = mix(0.5, 1.0, smoothstep(0.0, 0.2, min(along, 1.0 - along)));
    float w = width * taper;
    float core = exp(-dist * dist / (w * w));
    float glow = exp(-dist / halo);
    float bloom = 1.0 / (1.0 + dist * dist / 0.0025);
    float tip = age < LEADER ? exp(-length(vec2((travel - lead) * span, offset)) / 0.012) : 0.0;

    float branchCore = 0.0;
    float branchGlow = 0.0;
    for (int j = 0; j < BRANCHES; j++) {
        float bj = seed + 20.0 + float(j) * 5.31;
        float origin = mix(0.08, 0.85, hash11(bj));
        float originAlong = dir > 0.0 ? origin : 1.0 - origin;
        float ox = (originAlong - 0.5) * span;
        vec2 start = vec2(ox, channel(ox, span, seed, 6).x + bow(originAlong, span, seed));
        float reach = span * mix(0.12, 0.34, hash11(bj + 1.0));
        // Forks run ahead with the leader, mostly down toward the ground and sometimes up.
        float angle = mix(0.35, 1.05, hash11(bj + 2.0)) * (hash11(bj + 3.0) < 0.72 ? -1.0 : 1.0);
        vec2 forward = vec2(dir * cos(angle), sin(angle));
        vec2 rel = p - start;
        float bx = dot(rel, forward);
        float bt = bx / reach;
        // Skip only where the branch glow has fully decayed, or the cut shows as a straight seam.
        if (max(-bt, bt - 1.0) * reach > halo * 14.0) continue;
        vec2 bh = channel(bx, reach, bj, 4);
        float boff = perpendicular(
            abs(dot(rel, vec2(-forward.y, forward.x)) - (bh.x - channel(0.0, reach, bj, 4).x)), bh.y, width);
        float grown = clamp((age / LEADER - origin) * 5.0, 0.0, 1.0);
        float bover = max(-bt, bt - grown);
        float bdist = length(vec2(max(bover, 0.0) * reach, boff));
        float bw = width * mix(0.75, 0.3, clamp(bt, 0.0, 1.0));
        float strength = (1.0 - 0.75 * clamp(bt, 0.0, 1.0)) * step(0.001, grown);
        branchCore += exp(-bdist * bdist / (bw * bw)) * strength * 0.85;
        branchGlow += exp(-bdist / (halo * 1.1)) * strength * 0.5;
    }

    // White-hot core, theme colored glow. Low scud partly hides the channel.
    vec3 hot = mix(vec3(1.0), accent, 0.12);
    vec3 tint = mix(vec3(0.75, 0.82, 1.0), accent, 0.72);
    bolt += hot * ((core + branchCore) * 2.6 + tip * 1.5) * energy * (1.0 - 0.55 * front);
    bolt += tint * ((glow + branchGlow) * 0.9 + bloom * 0.35) * energy;
    flash += energy * (exp(-dist * 3.5) * 1.2 + 0.15);
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

    // Background behind the cloud gaps.
    vec3 zenith = vec3(0.012, 0.015, 0.024);
    vec3 haze = vec3(0.064, 0.071, 0.088);
    vec3 sky = mix(haze, zenith, smoothstep(0.0, 0.7, h));
    sky = mix(sky, sky * (0.55 + accent * 0.9), 0.34);

    // Flattened dome keeps clouds stable down to the horizon without a plane singularity.
    vec2 uv = d.xz / (max(h, 0.0) * 0.85 + 0.15);
    vec2 wind = vec2(0.045, 0.018) * t;
    vec3 q = vec3(uv * 0.42 + wind * 0.6, t * 0.015);
    vec2 warp = vec2(fbm(q, 4), fbm(q + vec3(5.2, 1.3, 7.7), 4)) - 0.5;
    vec3 cp = vec3(uv * 0.85 + warp * 1.9 + wind, t * 0.025);
    float base = fbm(cp, 6);
    float billow = 1.0 - abs(2.0 * noise3(cp * 2.7 + vec3(0.0, 0.0, t * 0.04)) - 1.0);
    float shape = base + 0.18 * (billow - 0.5);
    float density = smoothstep(0.28, 0.66, shape);
    float relief = clamp(0.5 + (base - fbm(cp + vec3(0.11, 0.0, -0.09), 3)) * 5.0, 0.0, 1.0);
    float front = smoothstep(0.42, 0.75, fbm(vec3(uv * 1.6 + wind * 2.4 + warp * 0.8, t * 0.05 + 3.0), 4));

    vec3 deep = vec3(0.018, 0.020, 0.028);
    vec3 lit = vec3(0.125, 0.135, 0.165);
    vec3 cloud = mix(deep, lit, relief * 0.75 + 0.25 * smoothstep(0.0, 0.5, h));
    cloud = mix(cloud, cloud * (0.6 + accent * 0.8), 0.3);
    vec3 color = mix(sky, cloud, density);
    color = mix(color, deep * 0.8, front * 0.45);
    // Distant cloud base dissolves into rain haze instead of ending at a hard horizon line.
    color = mix(color, haze, smoothstep(0.1, -0.02, h) * 0.65);

    // Rain shafts hanging below the cloud base near the horizon.
    float shafts = fbm(vec3(normalize(horizontal) * 4.0, h * 1.2 - t * 0.08), 3);
    float rain = smoothstep(0.32, 0.0, h) * smoothstep(-0.25, 0.0, h);
    color = mix(color, haze * 1.25, rain * smoothstep(0.42, 0.68, shafts) * 0.4);

    vec3 bolt = vec3(0.0);
    float flash = 0.0;
    float rate = max(LarpMotion.w, 0.05);
    for (int i = 0; i < CHANNELS; i++)
        strike(i, azimuth, elevation, pixel, rate, front, accent, bolt, flash);

    // Thin cloud edges scatter the flash toward the viewer and dense cores glow from within.
    vec3 flashTint = mix(vec3(0.62, 0.68, 0.85), accent, 0.55);
    color += flashTint * flash * mix(0.55, 1.0, relief) * (0.35 + 0.9 * density) * 0.5;
    color += flashTint * flash * (1.0 - density) * 0.12;
    color += bolt;

    // Below the horizon everything settles into dark rain haze.
    color = mix(color, haze * 0.6 + flashTint * flash * 0.04, smoothstep(0.0, -0.25, h));

    color = 1.0 - exp(-max(color, vec3(0.0)) * 1.15);
    OutColor = vec4(color, LarpColorAlpha.a);
}
