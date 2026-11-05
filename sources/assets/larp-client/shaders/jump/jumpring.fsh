#version 330 core



in vec2 vUV;
in vec4 vColor;
in vec2 vAux;
out vec4 fragColor;

layout(std140) uniform JumpParameters { vec4 JumpColorTime; };
#define uTime JumpColorTime.w
#define uStyle JUMP_STYLE
#define uColor2 JumpColorTime.rgb

const float TAU = 6.2831853;
float hash11(float p) { p = fract(p * 0.1031); p *= p + 33.33; p *= p + p; return fract(p); }
float ring(float r, float R, float w) { float x = (r - R) / w; return exp(-x * x); }


float sdTri(vec2 p, float s) {
    const float k = 1.7320508;
    p.x = abs(p.x) - s;
    p.y = p.y + s / k;
    if (p.x + k * p.y > 0.0) p = vec2(p.x - k * p.y, -k * p.x - p.y) / 2.0;
    p.x -= clamp(p.x, -2.0 * s, 0.0);
    return -length(p) * sign(p.y);
}

void main() {
    vec2  uv = vUV;
    float r  = length(uv);
    if (r > 1.25) discard;
    float p  = clamp(vAux.x, 0.0, 1.0);
    float seed = vAux.y;
    vec3  c1 = vColor.rgb;
    vec3  c2 = uColor2;
    float ang = atan(uv.y, uv.x);

    float life = pow(1.0 - p, 1.4) * smoothstep(0.0, 0.06, p);
    vec3 col = vec3(0.0);
    float a = 0.0;

    if (uStyle == 1) {
        float e = 1.0 - pow(1.0 - p, 3.0);
        float R = mix(0.08, 0.97, e);
        float w = 0.018 + 0.05 * p;
        float front = ring(r, R, w);
        float trail = smoothstep(R, R - 0.45, r) * step(r, R) * (1.0 - p) * 0.4;
        float streak = pow(0.5 + 0.5 * sin(ang * 23.0 + seed * 10.0), 8.0) * smoothstep(R, R - 0.25, r) * step(r, R);
        col = mix(c1, vec3(1.0), 0.55) * front * 1.6 + c1 * trail + c2 * streak * 0.5 * (1.0 - p);
    } else if (uStyle == 2) {
        float grow = smoothstep(0.0, 0.25, p);
        float rot = uTime * 0.6 + seed * TAU;
        float aw  = fract((ang + rot) / TAU);
        float px  = 0.012;
        float outer = smoothstep(px * 1.6, 0.0, abs(r - 0.93)) + smoothstep(px, 0.0, abs(r - 0.86)) * 0.8;
        float inner = smoothstep(px, 0.0, abs(r - 0.52));

        float seg  = floor(aw * 36.0);
        float glyph = step(0.35, hash11(seg + floor(seed * 50.0))) * step(0.875, r) * step(r, 0.915)
                    * step(0.15, fract(aw * 36.0)) * step(fract(aw * 36.0), 0.85);
        float ticks = step(0.86, r) * step(r, 0.93) * smoothstep(0.08, 0.0, abs(fract(aw * 72.0) - 0.5) - 0.42) * 0.6;

        float cr = cos(-rot * 1.5), sr = sin(-rot * 1.5);
        vec2  q = mat2(cr, -sr, sr, cr) * uv;
        float star = min(abs(sdTri(q, 0.44)), abs(sdTri(-q, 0.44)));
        float lines = smoothstep(px * 1.2, 0.0, star) * step(r, 0.55);

        float reveal = step(aw, grow);
        float shape = (outer + glyph * 0.9 + ticks + inner + lines) * reveal;
        float fill = smoothstep(0.93, 0.0, r) * 0.08;
        col = mix(c1, c2, smoothstep(0.4, 0.95, r)) * (shape * 1.3 + fill);
        a = fill * 0.5;
    } else if (uStyle == 3) {
        float open = 1.0 - pow(1.0 - smoothstep(0.0, 0.55, p), 3.0);
        float rot = uTime * 0.35 + seed * TAU + open * 0.8;
        float shape = 0.0;
        for (int layer = 0; layer < 2; layer++) {
            float fl = float(layer);
            float n = 6.0 + 6.0 * fl;
            float len = (0.9 - 0.32 * fl) * open;
            float aa = ang + rot * (fl == 0.0 ? 1.0 : -1.3) + fl * 0.26;
            float sector = mod(aa, TAU / n) - TAU / n * 0.5;
            vec2 q = vec2(cos(sector), sin(sector)) * r;
            float petal = length(vec2((q.x - len * 0.5) / (len * 0.5 + 1e-3), q.y / (0.16 * len + 0.02)));
            float outline = smoothstep(0.12, 0.0, abs(petal - 1.0)) * step(0.02, q.x);
            float fill = smoothstep(1.0, 0.2, petal) * 0.18;
            shape += (outline + fill) * (1.0 - fl * 0.35);
        }
        float core = exp(-r * r * 60.0) * (1.0 - p);
        float rim  = ring(r, 0.93 * open, 0.012) * 0.8;
        col = mix(c1, c2, smoothstep(0.1, 0.9, r)) * (shape * 1.25 + rim) + mix(c1, vec3(1.0), 0.7) * core * 1.2;
    } else if (uStyle == 4) {
        float e = 1.0 - pow(1.0 - smoothstep(0.0, 0.35, p), 3.0);
        float R = 0.5 * e;
        float band = ring(r, R, 0.022);
        float rays = 0.0;
        for (int k = 0; k < 2; k++) {
            float n = k == 0 ? 24.0 : 37.0;
            float jitter = hash11(floor((ang + 3.1416) / TAU * n) + seed * 40.0 + float(k) * 9.0);
            float spoke = pow(0.5 + 0.5 * cos(ang * n + seed * 6.0 + float(k) * 1.3), 18.0);
            float reach = R + (0.25 + 0.4 * jitter) * e * (0.8 + 0.2 * sin(uTime * 6.0 + jitter * 20.0));
            rays += spoke * smoothstep(reach, R, r) * step(R, r) * (k == 0 ? 1.0 : 0.6);
        }
        float glow = exp(-pow((r - R) * 5.0, 2.0)) * 0.25;
        col = mix(c1, vec3(1.0), 0.6) * band * 1.6 + mix(c1, c2, smoothstep(R, R + 0.5, r)) * (rays * 1.1 + glow);
    } else if (uStyle == 5) {
        float inP = smoothstep(0.0, 0.45, p), outP = smoothstep(0.45, 1.0, p);
        float twist = 6.0 * (1.0 - inP) + 1.5;
        float spin = uTime * 2.5 + seed * TAU;
        float arms = 0.0;
        for (int k = 0; k < 2; k++) {
            float s = sin((ang * 3.0 + log(r + 0.03) * twist * (k == 0 ? 1.0 : -1.0)) - spin * (k == 0 ? 1.0 : 0.7));
            arms += pow(0.5 + 0.5 * s, 10.0) * (k == 0 ? 1.0 : 0.5);
        }
        float reachIn = mix(1.0, 0.25, inP);
        arms *= smoothstep(reachIn, reachIn - 0.35, r) * (1.0 - outP);
        float core = exp(-r * r / (0.01 + 0.04 * inP)) * inP * (1.0 - outP);
        float burstR = 0.1 + 0.9 * (1.0 - pow(1.0 - outP, 3.0));
        float burst = ring(r, burstR, 0.03 + 0.04 * outP) * step(0.001, outP) * (1.0 - outP);
        col = mix(c2, c1, smoothstep(0.8, 0.1, r)) * arms * 1.2 + mix(c1, vec3(1.0), 0.7) * (core * 1.5 + burst * 1.4);
    } else if (uStyle == 6) {
        float e = 1.0 - pow(1.0 - p, 2.4);
        float R = mix(0.06, 0.94, e);
        float split = 0.035 + 0.06 * p;
        float ringA = ring(r, R, 0.02), ringB = ring(r, R - split, 0.02), ringC = ring(r, R - split * 2.0, 0.025);
        vec3 mid = mix(c1, c2, 0.5);
        float glitter = step(0.93, hash11(floor(ang * 40.0) + floor(uTime * 12.0) + seed * 70.0)) * ring(r, R, 0.045);
        float inner = smoothstep(R - split * 2.0, 0.0, r) * 0.06 * (1.0 - p);
        col = c1 * ringA * 1.4 + mid * ringB * 1.0 + c2 * ringC * 1.2 + vec3(1.0) * glitter * 1.4 + mid * inner;
    } else {
        for (int i = 0; i < 3; i++) {
            float k = float(i) * 0.16;
            float pk = clamp((p - k) / (1.0 - k), 0.0, 1.0);
            float e = 1.0 - pow(1.0 - pk, 2.2);
            float R = mix(0.05, 0.95, e);
            float amp = (1.0 - pk) * step(0.0001, pk) * (1.0 - float(i) * 0.25);
            col += mix(c1, c2, float(i) * 0.5) * ring(r, R, 0.03 + 0.03 * pk) * amp * 1.3;
        }
        col += c1 * smoothstep(0.6, 0.0, r) * (1.0 - p) * 0.12;
    }

    float edgeFade = smoothstep(1.25, 1.08, r);
    fragColor = vec4(col * life * edgeFade, a * life * edgeFade) * vColor.a;
}
