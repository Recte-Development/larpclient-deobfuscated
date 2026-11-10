#version 330
// Original Larp volcano: an erupting cone on the horizon, a lava-lit ash plume spreading into a
// storm deck, drifting embers, a blood-red sun behind the ash and violet lightning crawling
// through the smoke.
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
const float LEADER = 0.18;
const float LIFETIME = 1.3;
// Azimuth of the volcano and the height of its rim above the horizon.
const float VOLCANO = 2.6;
const float RIM = 0.2;

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

vec3 direction(float azimuth, float elevation) {
    return vec3(sin(azimuth) * cos(elevation), sin(elevation), cos(azimuth) * cos(elevation));
}

// Integer lattice hash: neighboring cells must agree bit for bit on their shared vertex.
float lattice(int i, float seed) {
    uint x = uint(i + 1048576) ^ (uint(seed * 97.0) * 0x9E3779B9u);
    x ^= x >> 16; x *= 0x7FEB352Du;
    x ^= x >> 15; x *= 0x846CA68Bu;
    x ^= x >> 16;
    return float(x) * (2.0 / 4294967296.0) - 1.0;
}

vec2 zigzag(float x, float seed) {
    float i = floor(x);
    float a = lattice(int(i), seed);
    float b = lattice(int(i) + 1, seed);
    return vec2(mix(a, b, x - i), b - a);
}

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

float perpendicular(float raw, float slope, float width) {
    return raw / mix(1.0, sqrt(1.0 + slope * slope), exp(-raw / (width * 1.5)));
}

float bow(float along, float span, float seed) {
    return 0.06 * span * sin(PI * clamp(along, 0.0, 1.0)) * (hash11(seed + 2.3) - 0.35);
}

// Volcanic lightning: bolts and glowing sheets confined to the plume around the vent.
void strike(int index, float azimuth, float elevation, float pixel, float rate, float front,
            vec3 accent, inout vec3 bolt, inout float flash) {
    float fi = float(index);
    float period = max((2.2 + 1.2 * fi) / rate, LIFETIME + 0.2);
    float clock = LarpTimeResolution.x + fi * 0.731 * period;
    float slot = floor(clock / period);
    float seed = fract(slot * 0.1373 + fi * 0.377) * 991.0;
    float kind = hash11(seed);
    float age = clock - slot * period - hash11(seed + 1.7) * (period - LIFETIME);
    if (kind > 0.82 || age < 0.0 || age > LIFETIME) return;

    float pulses = 1.0 + floor(hash11(seed + 8.9) * 3.0);
    float flicker = 0.0;
    for (int k = 0; k < 3; k++) {
        if (float(k) >= pulses) break;
        float onset = LEADER + float(k) * (0.09 + 0.06 * hash11(seed + 9.7 + float(k)));
        if (age >= onset) flicker = max(flicker, exp(-(age - onset) * 11.0) * (1.0 - 0.22 * float(k)));
    }
    float afterglow = age > LEADER ? exp(-(age - LEADER) * 2.6) : 0.0;
    float fade = smoothstep(LIFETIME, LIFETIME - 0.3, age);

    float az0 = VOLCANO + (hash11(seed + 3.1) - 0.5) * 1.5;
    float el0 = mix(0.3, 0.85, hash11(seed + 4.3));
    vec2 local = vec2((mod(azimuth - az0 + PI, TAU) - PI) * cos(el0), elevation - el0);

    if (kind > 0.52) {
        float sheet = exp(-dot(local, local) * 2.6);
        flash += (flicker + 0.2 * afterglow) * (sheet * 1.5 + 0.05) * fade;
        return;
    }

    float span = mix(0.7, 1.5, hash11(seed + 5.9));
    float tilt = (hash11(seed + 6.7) - 0.5) * 0.9 + 1.1 * (hash11(seed + 6.1) < 0.5 ? -1.0 : 1.0) * 0.0;
    float dir = hash11(seed + 7.3) < 0.5 ? -1.0 : 1.0;
    float c = cos(tilt), s = sin(tilt);
    vec2 p = vec2(c * local.x + s * local.y, -s * local.x + c * local.y);
    float along = p.x / span + 0.5;
    float travel = dir > 0.0 ? along : 1.0 - along;
    float lead = clamp(age / LEADER, 0.0, 1.0);
    float energy = (max(flicker, 0.22 * afterglow) + (age < LEADER ? 0.3 : 0.0)) * fade;

    float width = max(0.0024, pixel * 0.85);
    float halo = width * 7.0;

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
        float angle = mix(0.35, 1.05, hash11(bj + 2.0)) * (hash11(bj + 3.0) < 0.5 ? -1.0 : 1.0);
        vec2 forward = vec2(dir * cos(angle), sin(angle));
        vec2 rel = p - start;
        float bx = dot(rel, forward);
        float bt = bx / reach;
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

    // White-hot core in a violet-pink glow: volcanic lightning is charged ash, not rain.
    vec3 hot = mix(vec3(1.0, 0.96, 1.0), accent, 0.08);
    vec3 tint = mix(vec3(0.78, 0.45, 1.0), accent, 0.25);
    bolt += hot * ((core + branchCore) * 2.6 + tip * 1.5) * energy * (1.0 - 0.5 * front);
    bolt += tint * ((glow + branchGlow) * 0.95 + bloom * 0.4) * energy;
    flash += energy * (exp(-dist * 3.5) * 1.2 + 0.15);
}

// Rising embers in three depths; thickest near the vent.
vec3 embers(float azimuth, float elevation, float da, float t, float pixel) {
    vec3 sum = vec3(0.0);
    float near = exp(-da * da / 1.4);
    float coslat = max(cos(elevation), 0.2);
    for (int layer = 0; layer < 3; layer++) {
        float fl = float(layer) / 2.0;
        float columns = floor(mix(70.0, 190.0, fl));
        float cellW = TAU / columns;
        float cellH = mix(0.1, 0.045, fl);
        float rise = mix(0.14, 0.07, fl);
        float radius = mix(0.0042, 0.002, fl);
        // Wind carries the whole field slowly sideways.
        vec2 p = vec2((azimuth + 0.035 * t * (1.0 - 0.4 * fl)) / cellW, (elevation - rise * t) / cellH);
        vec2 id = vec2(mod(floor(p.x), columns), floor(p.y));
        vec2 f = fract(p);
        float h = hash12(id + float(layer) * 23.0);
        if (h > mix(0.015, 0.2, near)) continue;
        vec2 pos = vec2(0.5 + sin(t * (0.6 + h) + h * 50.0) * 0.2, 0.5 + (hash12(id + 4.0) - 0.5) * 0.4);
        vec2 delta = vec2((f.x - pos.x) * cellW * coslat, (f.y - pos.y) * cellH);
        float rad = max(radius * mix(0.5, 1.0, hash12(id + 2.0)), pixel * 0.85);
        float r = length(delta);
        float flake = exp(-r * r / (rad * rad)) * min(1.0, radius / rad);
        float flicker = 0.55 + 0.45 * sin(t * (3.0 + 4.0 * h) + h * 80.0);
        vec3 heat = mix(vec3(1.0, 0.26, 0.04), vec3(1.0, 0.78, 0.32), hash12(id + 7.0));
        float cool = mix(1.0, 0.25, smoothstep(0.1, 1.2, elevation));
        sum += heat * (flake * 2.2 + exp(-r / (rad * 5.0)) * 0.16) * flicker * cool;
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

    float da = mod(azimuth - VOLCANO + PI, TAU) - PI;
    vec2 local = vec2(da * cos(elevation), elevation);
    float reach = exp(-length(vec2(local.x, local.y - RIM)) / 0.6);
    vec3 lava = vec3(1.0, 0.33, 0.06);

    // Smoldering sky: black overhead, brick red where the whole horizon catches the eruption.
    vec3 zenith = vec3(0.011, 0.008, 0.011);
    vec3 haze = vec3(0.2, 0.062, 0.03) * 0.8;
    vec3 sky = mix(haze, zenith, smoothstep(0.0, 0.62, h));
    sky += lava * exp(-max(h, 0.0) / 0.13) * exp(-da * da / 1.6) * 0.3;
    sky = mix(sky, sky * (0.7 + accent * 0.6), 0.1);

    // A red sun behind the ash, visible only through gaps.
    vec3 sunDir = direction(VOLCANO - 1.7, 0.4);
    float sunAngle = acos(clamp(dot(d, sunDir), -1.0, 1.0));
    vec3 sun = vec3(1.0, 0.2, 0.05) * (smoothstep(0.034 + pixel, 0.034 - pixel, sunAngle) * 1.5 + exp(-sunAngle / 0.1) * 0.5 + exp(-sunAngle / 0.4) * 0.18);

    // Ash deck, thick and slow, underlit by the eruption.
    vec2 uv = d.xz / (max(h, 0.0) * 0.85 + 0.15);
    vec2 wind = vec2(0.05, 0.02) * t;
    vec3 q = vec3(uv * 0.42 + wind * 0.6, t * 0.015);
    vec2 warp = vec2(fbm(q, 3), fbm(q + vec3(5.2, 1.3, 7.7), 3)) - 0.5;
    vec3 cp = vec3(uv * 0.85 + warp * 1.9 + wind, t * 0.025);
    float base = fbm(cp, 5);
    float shape = base + 0.18 * (1.0 - abs(2.0 * noise3(cp * 2.7 + vec3(0.0, 0.0, t * 0.04)) - 1.0) - 0.5);
    float density = smoothstep(0.3, 0.58, shape);
    float relief = clamp(0.5 + (base - fbm(cp + vec3(0.11, 0.0, -0.09), 3)) * 5.0, 0.0, 1.0);
    vec3 cloud = mix(vec3(0.006, 0.005, 0.006), vec3(0.05, 0.04, 0.042), relief * 0.7 + 0.3 * smoothstep(0.0, 0.5, h));
    cloud += lava * reach * pow(relief, 1.6) * 0.75 + vec3(0.5, 0.12, 0.04) * exp(-max(h, 0.0) / 0.2) * 0.05;
    vec3 color = mix(sky + sun * (1.0 - density) * (1.0 - density), cloud, density);

    // The plume: a trunk of ash lit from the crater below that mushrooms into the deck.
    float ex = local.x, ey = local.y;
    float trunk = mix(0.12 + 0.22 * ey, 1.1, smoothstep(0.7, 1.2, ey));
    vec3 pp = vec3(vec2(ex * 3.4, ey * 3.0 - t * 0.2), t * 0.07);
    vec2 pw = vec2(fbm(pp + vec3(0.0, 0.0, 3.0), 3), fbm(pp + vec3(4.1, 2.3, 1.0), 3)) - 0.5;
    vec3 pq = pp + vec3(pw * 1.5, 0.0);
    float pn = fbm(pq, 5);
    float lateral = abs(ex - 0.05 * sin(ey * 4.0 + t * 0.15)) / trunk;
    float column = smoothstep(1.0, 0.25, lateral) * smoothstep(0.04, 0.17, ey) * smoothstep(1.45, 1.05, ey);
    float plume = smoothstep(0.34, 0.6, column * 0.95 + (pn - 0.5) * 1.5);
    float plumeRelief = clamp(0.5 + (pn - fbm(pq + vec3(0.12, -0.1, 0.0), 3)) * 5.0, 0.0, 1.0);
    float underlit = exp(-max(ey - RIM, 0.0) / 0.5);
    vec3 smoke = mix(vec3(0.006, 0.005, 0.006), vec3(0.05, 0.04, 0.042), plumeRelief * 0.7);
    smoke += lava * underlit * pow(plumeRelief, 1.2) * 1.2 + vec3(1.0, 0.5, 0.2) * smoothstep(0.5, 0.0, ey - RIM) * 0.1;
    color = mix(color, smoke, plume);
    float cover = max(density, plume);

    // The cone itself: dark rock, a glowing rim and rivers of lava running down the flanks.
    float slope = pow(max(1.0 - abs(da) / 0.75, 0.0), 1.3);
    float rock = RIM * slope + 0.012 * (fbm(vec3(da * 11.0, 1.7, 0.0), 3) - 0.5) * smoothstep(0.0, 0.5, 1.0 - slope);
    rock -= 0.018 * smoothstep(0.06, 0.0, abs(da));
    float mountain = smoothstep(pixel, -pixel, elevation - rock);
    float veins = fbm(vec3(da * 44.0, elevation * 2.2 - t * 0.03, 4.3), 4);
    float river = smoothstep(0.55, 0.63, veins) * exp(-abs(da) / 0.2) * smoothstep(0.0, 0.03, rock - elevation) * smoothstep(-0.01, 0.1, elevation);
    float pulse = 0.65 + 0.35 * sin(t * 0.7 + veins * 12.0);
    vec3 river_color = mix(vec3(0.8, 0.1, 0.02), vec3(1.0, 0.7, 0.28), smoothstep(0.55, 0.7, veins) * pulse);
    vec3 cone = vec3(0.005, 0.004, 0.005) + lava * 0.03 * exp(-abs(da) / 0.18) * smoothstep(0.0, RIM, elevation) + river_color * river * 1.5;
    vec2 craterAt = vec2(da * cos(RIM), elevation - (RIM - 0.01));
    float craterGlow = exp(-length(craterAt) / 0.024) * 2.4 + exp(-length(craterAt * vec2(0.7, 1.0)) / 0.2) * 0.45;
    float broadGlow = exp(-length(craterAt * vec2(0.7, 1.0)) / 0.2) * 0.45;
    color += vec3(1.0, 0.45, 0.1) * broadGlow;
    color = mix(color, cone, mountain);
    color += vec3(1.0, 0.45, 0.1) * (craterGlow - broadGlow) * (0.8 + 0.2 * sin(t * 2.3));

    // Two ridges of foothills against the glowing horizon.
    float far = 0.03 + 0.035 * fbm(vec3(azimuth * 3.4, 0.5, 0.0), 4);
    float nearRidge = 0.008 + 0.028 * fbm(vec3(azimuth * 5.5, 9.0, 0.0), 4);
    vec3 farColor = mix(vec3(0.02, 0.012, 0.012), haze, 0.28) + lava * 0.03 * exp(-da * da / 0.9);
    color = mix(color, farColor, smoothstep(pixel, -pixel, elevation - far) * (1.0 - mountain));
    color = mix(color, vec3(0.005, 0.004, 0.005) + lava * 0.012 * exp(-da * da / 1.2), smoothstep(pixel, -pixel, elevation - nearRidge));

    color += embers(azimuth, elevation, da, t, pixel) * smoothstep(-0.01, 0.03, elevation);

    vec3 bolt = vec3(0.0);
    float flash = 0.0;
    float rate = max(LarpMotion.w, 0.05);
    for (int i = 0; i < CHANNELS; i++)
        strike(i, azimuth, elevation, pixel, rate, plume, accent, bolt, flash);

    vec3 flashTint = mix(vec3(0.8, 0.5, 0.95), accent, 0.25);
    color += flashTint * flash * mix(0.5, 1.0, relief) * (0.3 + 0.9 * cover) * 0.5;
    color += flashTint * flash * (1.0 - cover) * 0.1;
    color += bolt;

    // Below the horizon everything is ash haze lit from the vent side.
    vec3 floorHaze = haze * 0.55 + lava * 0.03 * exp(-da * da / 1.4);
    color = mix(color, floorHaze, smoothstep(-0.015, -0.08, h));

    color = 1.0 - exp(-max(color, vec3(0.0)) * 1.2);
    OutColor = vec4(color, LarpColorAlpha.a);
}
