#version 330
// Original Larp Christmas: a moonlit snowfall over a fir forest strung with lights, red and green
// aurora, a guiding star and the odd shooting star.
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

// Unit vector for an azimuth/elevation pair, matching atan(x, z) and asin(y) in main().
vec3 direction(float azimuth, float elevation) {
    return vec3(sin(azimuth) * cos(elevation), sin(elevation), cos(azimuth) * cos(elevation));
}

// Tangent frame around a sky direction: x = right, y = up, z = toward the direction.
void frame(vec3 center, out vec3 right, out vec3 up) {
    right = normalize(cross(vec3(0.0, 1.0, 0.0), center));
    up = cross(center, right);
}

vec3 stars(vec3 d, float t, float pixel) {
    vec3 sum = vec3(0.0);
    for (int layer = 0; layer < 3; layer++) {
        float fl = float(layer);
        float cells = 34.0 + 30.0 * fl;
        vec3 p = d * cells + fl * 17.0;
        vec3 id = floor(p);
        vec3 f = fract(p);
        float h = hash13(id + 3.7);
        if (h > 0.34) continue;
        vec3 pos = 0.2 + 0.6 * vec3(hash13(id + 1.3), hash13(id + 2.7), hash13(id + 4.1));
        float dist = length(f - pos) / cells;
        float size = mix(0.0012, 0.0026, hash13(id + 9.1)) * (1.0 - 0.25 * fl);
        float rad = max(size, pixel * 0.8);
        float twinkle = 0.7 + 0.3 * sin(t * (1.5 + 3.0 * h) + h * 90.0);
        float shine = exp(-dist * dist / (rad * rad)) * min(1.0, size / rad) * twinkle;
        float warm = hash13(id + 6.1);
        vec3 tint = warm < 0.3 ? vec3(1.0, 0.86, 0.62) : (warm > 0.8 ? vec3(0.72, 0.84, 1.0) : vec3(1.0));
        sum += tint * shine * mix(0.6, 1.7, hash13(id + 7.7));
    }
    return sum;
}

// Curtains of green fringed with red, the same colors as the season.
vec3 aurora(float azimuth, float elevation, float t) {
    vec3 sum = vec3(0.0);
    for (int k = 0; k < 2; k++) {
        float fk = float(k);
        float sector = mod(azimuth - (0.9 + fk * 2.7) + PI, TAU) - PI;
        float envelope = exp(-sector * sector / (1.8 - 0.5 * fk)) * smoothstep(1.3, 0.8, elevation);
        if (envelope < 0.01) continue;
        float fold = noise3(vec3(sector * 2.1 + fk * 7.0, t * 0.05, fk * 3.1));
        float wander = 0.2 + 0.1 * fk + 0.1 * sin(sector * 1.9 + t * 0.05 * (1.0 + fk) + fk * 2.0)
                     + 0.06 * sin(sector * 4.3 - t * 0.08 + fk);
        float y = elevation - wander - 0.14 * (fold - 0.5);
        float profile = smoothstep(-0.02, 0.04, y) * exp(-max(y, 0.0) / 0.2);
        float rays = noise3(vec3(sector * 52.0 + 4.0 * fold, y * 2.5 - t * 0.12, fk * 5.3));
        rays = 0.3 + 0.7 * smoothstep(0.3, 0.85, rays);
        float pulse = 0.65 + 0.35 * sin(t * 0.35 + sector * 3.0 + fk * 2.0);
        vec3 green = vec3(0.16, 1.0, 0.44);
        vec3 red = vec3(1.0, 0.12, 0.26);
        vec3 tint = mix(green, red, smoothstep(0.04, 0.4, y));
        tint = mix(tint, vec3(0.9, 1.0, 0.85), smoothstep(0.1, 0.0, y) * 0.35);
        float fringe = smoothstep(0.1, 0.34, y) * exp(-max(y - 0.34, 0.0) / 0.16);
        sum += tint * profile * rays * pulse * envelope * (0.55 - 0.12 * fk);
        sum += red * fringe * rays * pulse * envelope * (0.17 - 0.04 * fk);
    }
    return sum;
}

// Four-point star with a soft halo, the one the sky is arranged around.
vec3 guidingStar(vec3 d, float t) {
    vec3 c = direction(-0.95, 0.83);
    float cosine = dot(d, c);
    if (cosine < 0.9) return vec3(0.0);
    vec3 right, up;
    frame(c, right, up);
    vec2 q = vec2(dot(d, right), dot(d, up));
    float r = length(q);
    float pulse = 0.88 + 0.12 * sin(t * 1.1);
    float core = exp(-r / 0.0045);
    float halo = 0.0016 / (r * r + 0.00012);
    float arms = exp(-abs(q.y) / 0.0022) * exp(-abs(q.x) / 0.075) + exp(-abs(q.x) / 0.0022) * exp(-abs(q.y) / 0.075);
    vec2 s = vec2(q.x + q.y, q.x - q.y) * 0.7071;
    float diagonals = exp(-abs(s.y) / 0.0018) * exp(-abs(s.x) / 0.028) + exp(-abs(s.x) / 0.0018) * exp(-abs(s.y) / 0.028);
    vec3 gold = vec3(1.0, 0.82, 0.48);
    return (gold * (halo * 0.6 + arms * 0.55 + diagonals * 0.3) + vec3(1.0, 0.97, 0.9) * core * 2.4) * pulse;
}

vec3 moon(vec3 d, float pixel, float cirrus) {
    vec3 c = direction(2.35, 0.43);
    float cosine = dot(d, c);
    float angle = acos(clamp(cosine, -1.0, 1.0));
    float radius = 0.058;
    vec3 sum = vec3(0.0);
    float veil = 1.0 - 0.45 * cirrus;
    sum += vec3(0.55, 0.68, 0.95) * (0.0075 / (angle * angle + 0.0075) * 0.22 + exp(-angle / 0.35) * 0.18) * veil;
    // The 22 degree ice halo is faint and slightly split by color.
    float ring = angle - 0.385;
    sum += vec3(exp(-pow((ring + 0.008) / 0.02, 2.0)) * 0.7, exp(-pow(ring / 0.02, 2.0)) * 0.65, exp(-pow((ring - 0.008) / 0.02, 2.0))) * 0.075 * veil;
    if (angle < radius + pixel * 2.0) {
        vec3 right, up;
        frame(c, right, up);
        vec2 q = vec2(dot(d, right), dot(d, up)) / radius;
        float rr = dot(q, q);
        float z = sqrt(max(1.0 - rr, 0.0));
        vec3 n = q.x * right + q.y * up + z * c;
        float maria = smoothstep(0.46, 0.62, fbm(n * 2.1 + 4.0, 4));
        float craters = fbm(n * 7.0 + 1.0, 3);
        float surface = mix(0.92, 0.5, maria * 0.75) * (0.88 + 0.24 * craters) * (0.35 + 0.65 * pow(z, 0.45));
        float edge = smoothstep(1.0 + pixel / radius * 1.2, 1.0 - pixel / radius * 1.2, sqrt(rr));
        sum += vec3(1.0, 0.96, 0.88) * surface * edge * 1.4 * mix(1.0, 0.7, cirrus);
    }
    return sum;
}

// One shooting star in a slot of eleven seconds, gold-white with a fading tail.
vec3 shootingStar(float azimuth, float elevation, float t, float pixel) {
    float period = 11.0;
    float slot = floor(t / period);
    float h = hash11(slot * 1.37);
    float age = t - slot * period - h * 6.0;
    if (hash11(slot * 2.11 + 4.0) > 0.7 || age < 0.0 || age > 0.8) return vec3(0.0);
    float az0 = hash11(slot * 3.3 + 1.0) * TAU;
    float el0 = mix(0.55, 1.0, hash11(slot * 5.1 + 2.0));
    float angle = (hash11(slot * 7.7 + 3.0) - 0.5) * 1.2 - 1.2;
    vec2 dir = vec2(cos(angle), sin(angle));
    vec2 local = vec2((mod(azimuth - az0 + PI, TAU) - PI) * cos(el0), elevation - el0);
    float travel = age * 0.9;
    float tail = 0.08 + 0.16 * min(age / 0.15, 1.0);
    vec2 head = dir * travel;
    vec2 from = head - dir * tail;
    vec2 pa = local - from, ba = head - from;
    float u = clamp(dot(pa, ba) / dot(ba, ba), 0.0, 1.0);
    float dist = length(pa - ba * u);
    float width = max(0.0014, pixel * 0.8);
    float fade = smoothstep(0.8, 0.45, age) * smoothstep(0.0, 0.06, age);
    float body = exp(-dist * dist / (width * width)) * pow(u, 2.2);
    float halo = exp(-dist / (width * 5.0)) * u * u * 0.25;
    return vec3(1.0, 0.9, 0.7) * (body * 2.6 + halo) * fade;
}

// Falling snow in four depths, each drifting and swaying at its own pace.
float snow(float azimuth, float elevation, float t, float pixel) {
    float sum = 0.0;
    float fade = smoothstep(1.25, 0.8, elevation);
    if (fade <= 0.0) return 0.0;
    float coslat = max(cos(elevation), 0.2);
    for (int layer = 0; layer < 4; layer++) {
        float fl = float(layer);
        float columns = floor(mix(46.0, 190.0, fl / 3.0));
        float cellW = TAU / columns;
        float cellH = mix(0.13, 0.04, fl / 3.0);
        float radius = mix(0.0105, 0.0032, fl / 3.0);
        float fall = mix(0.22, 0.075, fl / 3.0);
        vec2 p = vec2(azimuth / cellW, (elevation + fall * t) / cellH);
        float col = floor(p.x);
        float row = floor(p.y);
        vec2 id = vec2(mod(col, columns), row);
        vec2 f = fract(p);
        float h = hash12(id + fl * 31.0);
        if (h > 0.62) continue;
        float sway = sin(t * (0.5 + h) + h * 40.0) * 0.18;
        vec2 pos = vec2(0.5 + sway + (hash12(id + 5.0) - 0.5) * 0.25, 0.5 + (hash12(id + 9.0) - 0.5) * 0.5);
        vec2 delta = vec2((f.x - pos.x) * cellW * coslat, (f.y - pos.y) * cellH);
        float rad = max(radius * mix(0.6, 1.0, hash12(id + 2.0)), pixel * 0.9);
        float flake = smoothstep(rad, rad * mix(0.15, 0.55, 1.0 - fl / 3.0), length(delta));
        flake *= min(1.0, radius / rad);
        sum += flake * mix(0.28, 0.55, fl / 3.0) * (0.6 + 0.4 * sin(t * 3.0 + h * 60.0));
    }
    return sum * fade;
}

struct Tree {
    float x;
    float width;
    float height;
};

Tree tree(int index, int count, float seed, float lo, float hi, float widen) {
    float fi = float(index);
    float n = float(count);
    float cell = TAU / n;
    float j = hash11(fi * 1.91 + seed);
    Tree result;
    result.x = (fi + 0.5 + (j - 0.5) * 0.7) * cell;
    result.width = cell * mix(0.7, 1.15, hash11(fi * 4.3 + seed + 1.0)) * widen;
    result.height = mix(lo, hi, hash11(fi * 7.1 + seed + 2.0));
    return result;
}

// Fir silhouette: stacked tiers that narrow toward the top. Returns coverage and the depth below the edge.
float firInside(Tree t, float azimuth, float elevation) {
    float dx = mod(azimuth - t.x + PI, TAU) - PI;
    float y = elevation / t.height;
    if (y < 0.0) return 0.2;
    if (y > 1.0) return -(y - 1.0) * t.height - abs(dx) * 0.5;
    float tier = fract(y * 6.0);
    float halfWidth = t.width * (1.0 - y) * (1.0 - 0.3 * tier);
    return halfWidth - abs(dx);
}

vec3 lightColor(float pick) {
    if (pick < 0.2) return vec3(1.0, 0.1, 0.12);
    if (pick < 0.4) return vec3(1.0, 0.72, 0.22);
    if (pick < 0.6) return vec3(0.15, 1.0, 0.35);
    if (pick < 0.8) return vec3(0.25, 0.5, 1.0);
    return vec3(1.0, 0.35, 0.75);
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

    // Deep midnight overhead, lifting to snow-lit blue and a warm village glow at the horizon.
    vec3 zenith = vec3(0.045, 0.02, 0.17);
    vec3 mid = vec3(0.03, 0.15, 0.3);
    vec3 low = vec3(0.34, 0.17, 0.3);
    vec3 sky = mix(low, mid, smoothstep(0.0, 0.3, h));
    sky = mix(sky, zenith, smoothstep(0.25, 0.95, h));
    float village = 0.7 + 0.3 * noise3(vec3(azimuth * 3.0, 1.7, 0.0));
    sky += vec3(1.0, 0.5, 0.16) * exp(-max(h, 0.0) / 0.07) * 0.34 * village;
    // Soft emerald and crimson glows drifting through the night like colored mist.
    vec3 mistP = vec3(d.xz * 1.6 + vec2(t * 0.01, 0.0), h * 1.8 + t * 0.012);
    float mistA = smoothstep(0.35, 0.75, fbm(mistP, 4));
    float mistB = smoothstep(0.35, 0.75, fbm(mistP * 1.1 + vec3(7.3, 2.1, 4.0), 4));
    sky += (vec3(0.02, 0.28, 0.15) * mistA + vec3(0.32, 0.03, 0.1) * mistB) * smoothstep(-0.02, 0.2, h) * smoothstep(1.3, 0.3, h) * 0.8;
    sky = mix(sky, sky * (0.6 + accent * 0.8), 0.14);

    // High wispy cloud, silver where the moon lights it.
    vec2 uv = d.xz / (max(h, 0.0) * 0.85 + 0.2);
    float cirrus = smoothstep(0.42, 0.78, fbm(vec3(uv * vec2(0.5, 1.25) + vec2(t * 0.012, 0.0), t * 0.01), 4)) * smoothstep(0.0, 0.25, h);
    vec3 moonDir = direction(2.35, 0.43);
    float moonLight = pow(max(dot(d, moonDir), 0.0), 6.0);
    sky += vec3(0.28, 0.34, 0.5) * cirrus * (0.1 + 0.9 * moonLight) * 0.5;

    float starFade = smoothstep(0.02, 0.35, h) * (1.0 - 0.6 * cirrus);
    vec3 color = sky + stars(d, t, pixel) * starFade;

    vec3 northern = aurora(azimuth, elevation, t) * smoothstep(0.0, 0.12, h);
    color += northern;
    color += moon(d, pixel, cirrus) * smoothstep(-0.02, 0.0, h);
    color += guidingStar(d, t);
    color += shootingStar(azimuth, elevation, t, pixel) * smoothstep(0.0, 0.2, h);

    // Snow hangs in the air, brighter near the village glow and over the aurora.
    float flakes = snow(azimuth, elevation, t, pixel);
    color += vec3(0.78, 0.88, 1.0) * flakes * (0.7 + 0.5 * exp(-max(h, 0.0) / 0.2)) + vec3(0.2, 0.3, 0.2) * flakes * length(northern);

    // Two ridges of firs: a hazy far one and a dark near one carrying the lights.
    vec3 haze = vec3(0.2, 0.15, 0.3);
    haze = mix(haze, haze * (0.6 + accent * 0.8), 0.14);
    if (elevation < 0.4) {
        int farCount = 96, nearCount = 44;
        int farBase = int(floor(azimuth / (TAU / float(farCount))));
        float farInside = -1.0;
        for (int i = -2; i <= 2; i++)
            farInside = max(farInside, firInside(tree((farBase + i + farCount) % farCount, farCount, 11.0, 0.07, 0.17, 1.6), azimuth, elevation));
        vec3 farColor = mix(vec3(0.02, 0.035, 0.08), haze, 0.18);
        color = mix(color, farColor, smoothstep(-pixel, pixel, farInside));

        int nearBase = int(floor(azimuth / (TAU / float(nearCount))));
        float nearInside = -1.0;
        vec3 bulbs = vec3(0.0);
        for (int i = -2; i <= 2; i++) {
            int index = (nearBase + i + nearCount) % nearCount;
            Tree tr = tree(index, nearCount, 53.0, 0.11, 0.27, 1.25);
            nearInside = max(nearInside, firInside(tr, azimuth, elevation));
            float fi = float(index);
            float dx = mod(azimuth - tr.x + PI, TAU) - PI;
            for (int k = 0; k < 6; k++) {
                float fk = float(k);
                float seed = fi * 13.7 + fk * 3.3;
                float y = (fk + 0.35 + 0.4 * hash11(seed)) / 6.2 * 0.82;
                float tier = fract(y * 6.0);
                float reach = tr.width * (1.0 - y) * (1.0 - 0.3 * tier) * 0.82;
                vec2 at = vec2(tr.x + (hash11(seed + 1.0) * 2.0 - 1.0) * reach, y * tr.height);
                vec2 delta = vec2(dx - (at.x - tr.x), elevation - at.y);
                float r = length(delta);
                float rad = max(0.0016, pixel * 0.9);
                float pick = hash11(seed + 2.0);
                // Neighboring colors chase each other along the string.
                float blink = 0.55 + 0.45 * sin(t * (1.1 + 0.9 * floor(pick * 5.0)) + fi * 0.7 + fk * 1.3);
                vec3 lamp = lightColor(pick);
                bulbs += lamp * (exp(-r * r / (rad * rad)) * 2.4 + exp(-r / 0.014) * 0.34) * blink;
            }
        }
        float nearCover = smoothstep(-pixel, pixel, nearInside);
        float rim = smoothstep(0.012, 0.0, nearInside);
        vec3 nearColor = vec3(0.01, 0.017, 0.035) + vec3(0.1, 0.14, 0.24) * rim * 0.5;
        // The forest floor melts into the haze so the fog sees one even color below the horizon.
        color = mix(color, nearColor, nearCover * smoothstep(-0.07, -0.01, elevation));
        color += bulbs * smoothstep(-0.05, 0.0, elevation);
    }

    // Everything below the horizon settles into snowy haze for the fog to blend with.
    color = mix(color, haze, smoothstep(-0.01, -0.07, h));

    color = 1.0 - exp(-max(color, vec3(0.0)) * 1.25);
    OutColor = vec4(color, LarpColorAlpha.a);
}
