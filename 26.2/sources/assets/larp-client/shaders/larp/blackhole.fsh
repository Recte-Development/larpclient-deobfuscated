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

const int ITERATIONS = 120;
const float FAR = 16.0;
const float ENTRY_R = 8.0;
const float CAM_DIST = 14.0;
const float INCL = 0.10;
const float EXPOSURE = 0.024;
const float SPIN = 0.16;
const float TAU = 6.28318531;

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c);
}
mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(c, 0.0, s, 0.0, 1.0, 0.0, -s, 0.0, c);
}

vec3 nmzHash33(vec3 q) {
    uvec3 p = uvec3(ivec3(q));
    p = p * uvec3(374761393U, 1103515245U, 668265263U) + p.zxy + p.yzx;
    p = p.yzx * (p.zxy ^ (p >> 3U));
    return vec3(p ^ (p >> 16U)) * (1.0 / vec3(0xffffffffU));
}

float hash21(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise3D(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    vec3 n000 = nmzHash33(i);
    vec3 n100 = nmzHash33(i + vec3(1,0,0));
    vec3 n010 = nmzHash33(i + vec3(0,1,0));
    vec3 n110 = nmzHash33(i + vec3(1,1,0));
    vec3 n001 = nmzHash33(i + vec3(0,0,1));
    vec3 n101 = nmzHash33(i + vec3(1,0,1));
    vec3 n011 = nmzHash33(i + vec3(0,1,1));
    vec3 n111 = nmzHash33(i + vec3(1,1,1));
    float a = mix(mix(n000.x, n100.x, f.x), mix(n010.x, n110.x, f.x), f.y);
    float b = mix(mix(n001.x, n101.x, f.x), mix(n011.x, n111.x, f.x), f.y);
    return mix(a, b, f.z);
}

vec3 discCoord(float ang, float rad, float freq) {
    float a = 1.425 * freq;
    return vec3(cos(ang) * a, sin(ang) * a, rad * freq);
}

float octave(float ang, float rad, float freq) {
    return noise3D(discCoord(ang, rad, freq));
}

float pcurve(float x, float a, float b) {
    float k = pow(a + b, a + b) / (pow(a, a) * pow(b, b));
    return k * pow(x, a) * pow(1.0 - x, b);
}

float sdTorus(vec3 p, vec2 t) {
    vec2 q = vec2(length(p.xz) - t.x, p.y);
    return length(q) - t.y;
}

vec3 starField(vec3 dir, float time) {
    vec3 c = vec3(0.0);
    vec3 p = dir * 42.0;
    float dens = 0.05;
    float amp = 1.0;
    for (int i = 0; i < 4; i++) {
        vec3 id = floor(p);
        vec3 q = fract(p) - 0.5;
        vec3 rn = nmzHash33(id);
        float core = smoothstep(0.44, 0.05, length(q));
        float hit = step(rn.x, dens);
        float tw = 0.80 + 0.20 * sin(time * (0.6 + rn.z * 1.3) + rn.y * 47.0);
        vec3 tint = mix(vec3(1.0, 0.72, 0.45), vec3(0.72, 0.86, 1.0), rn.y);
        c += hit * core * core * tint * (0.35 + 0.65 * rn.z) * tw * amp;
        p = p * 1.63 + 17.0;
        dens *= 0.62;
        amp *= 0.78;
    }
    return c;
}

void Haze(inout vec3 color, vec3 pos, float alpha, vec3 mainColor) {
    if (dot(pos, pos) > 36.0) {
        return;
    }
    vec2 t = vec2(1.0, 0.01);
    float torusDist = length(sdTorus(pos + vec3(0.0, -0.05, 0.0), t));
    float bloomDisc = 1.0 / (pow(torusDist, 2.0) + 0.001);
    bloomDisc *= length(pos) < 0.5 ? 0.0 : 1.0;
    color += mainColor * bloomDisc * (2.9 / float(ITERATIONS)) * (1.0 - alpha);
}

void GasDisc(inout vec3 color, inout float alpha, vec3 pos, float time, vec3 mainColor, vec3 eyevec) {
    float discRadius = 3.2;
    float discWidth = 5.3;
    float discInner = discRadius - discWidth * 0.5;

    vec3 discNormal = vec3(0.0, 1.0, 0.0);
    float discThickness = 0.1;

    float distFromCenter = length(pos);
    if (distFromCenter > 7.0) {
        return;
    }
    float distFromDisc = dot(discNormal, pos);

    float radialGradient = 1.0 - clamp((distFromCenter - discInner) / discWidth * 0.5, 0.0, 1.0);
    float coverage = pcurve(radialGradient, 4.0, 0.9);

    discThickness *= radialGradient;
    coverage *= clamp(1.0 - abs(distFromDisc) / max(discThickness, 1e-5), 0.0, 1.0);

    vec3 tangent = normalize(vec3(-pos.z, 0.0, pos.x));
    float dop = pow(clamp(1.0 - 0.55 * dot(tangent, eyevec), 0.45, 1.9), -2.5);
    vec3 dopTint = mix(vec3(1.30, 0.72, 0.45), vec3(0.80, 0.92, 1.45), clamp((dop - 0.7) * 0.9, 0.0, 1.0));

    vec3 dustColorLit = mainColor * dop * dopTint;
    float dustGlow = 1.0 / (pow(1.0 - radialGradient, 2.0) * 290.0 + 0.002);
    vec3 dustColor = dustColorLit * dustGlow * 8.2;

    coverage = clamp(coverage * 0.7, 0.0, 1.0);

    float fade = pow((abs(distFromCenter - discInner) + 0.4), 4.0) * 0.04;
    float bloomFactor = 1.0 / (pow(distFromDisc, 2.0) * 40.0 + fade + 0.00002);
    vec3 b = dustColorLit * pow(bloomFactor, 1.5);

    b *= mix(vec3(1.7, 1.1, 1.0), vec3(0.5, 0.6, 1.0), vec3(pow(radialGradient, 2.0)));
    b *= mix(vec3(1.7, 0.5, 0.1), vec3(1.0), vec3(pow(radialGradient, 0.5)));

    dustColor = mix(dustColor, b * 150.0, clamp(1.0 - coverage, 0.0, 1.0));
    coverage = clamp(coverage + bloomFactor * bloomFactor * 0.1, 0.0, 1.0);

    if (coverage < 0.01) {
        return;
    }

    float ang = atan(-pos.x, -pos.z);
    float rad = (distFromCenter * 1.5 + 0.55 + distFromDisc * 1.5) * 0.95 + time * 0.012;

    float omega = SPIN * pow(3.2 / max(distFromCenter, 0.75), 1.5);
    float angA = ang + mod(time * omega, TAU);
    float angB = ang + mod(time * omega * 0.45, TAU);

    float n1 = 1.0;
    n1 *= octave(angA, rad, 3.0);
    n1 *= octave(angB, rad, 6.0);
    n1 *= octave(angA, rad, 12.0);

    float n2 = 2.0;
    float rad2 = rad + 30.0;
    n2 *= octave(angB, rad2, 3.0);
    n2 *= octave(angA, rad2, 6.0);
    n2 *= octave(angB, rad2, 12.0);

    dustColor *= n1 * 0.998 + 0.002;
    coverage *= n2;

    float bandAng = ang + mod(time * omega * 0.5, TAU);
    float band = octave(bandAng, rad, 1.35);
    float grain = octave(bandAng, rad + 70.0, 3.46);
    vec3 texCol = mix(vec3(0.95, 0.55, 0.26), vec3(0.42, 0.60, 1.0), band) * (0.45 + 0.80 * grain);
    dustColor *= pow(texCol, vec3(2.0)) * 4.0;

    float arm = 0.5 + 0.5 * cos(2.0 * ang + 2.6 * log(max(distFromCenter, 0.3)) + time * SPIN * 4.5);
    dustColor *= 0.40 + 1.05 * arm * arm;
    coverage *= 0.78 + 0.22 * arm;

    coverage = clamp(coverage * 1200.0 / float(ITERATIONS), 0.0, 1.0);
    dustColor = max(vec3(0.0), dustColor);

    coverage *= pcurve(radialGradient, 4.0, 0.9);

    color = (1.0 - alpha) * dustColor * coverage + color;
    alpha = (1.0 - alpha) * coverage + alpha;
}

void WarpSpace(inout vec3 eyevec, vec3 raypos) {
    float singularityDist = length(raypos);
    float warpFactor = 1.0 / (pow(singularityDist, 2.0) + 0.000001);
    vec3 singularityVector = -raypos / max(singularityDist, 1e-4);
    float warpAmount = 5.0;
    eyevec = normalize(eyevec + singularityVector * warpFactor * warpAmount / float(ITERATIONS));
}

void main() {
    float time = uTime * uSpeed;
    vec2 sp = (gl_FragCoord.xy / uResolution.xy) * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;

    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(-sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 rd = rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV;

    vec3 bhDir = normalize(vec3(0.34, 0.62, 0.71));
    vec3 upRef = vec3(0.0, 1.0, 0.0);
    vec3 tang = normalize(upRef - dot(upRef, bhDir) * bhDir);
    vec3 eY = normalize(tang * cos(INCL) - bhDir * sin(INCL));
    vec3 eX = normalize(cross(eY, bhDir));
    vec3 eZ = cross(eX, eY);

    vec3 mainColor = mix(vec3(1.0), uColor, 0.6);

    vec3 camWorld = -bhDir * CAM_DIST;
    vec3 pos = vec3(dot(camWorld, eX), dot(camWorld, eY), dot(camWorld, eZ));
    vec3 eyevec = vec3(dot(rd, eX), dot(rd, eY), dot(rd, eZ));

    vec3 color = starField(rd, time) * 0.6;
    float alpha = 0.0;
    float captured = 0.0;

    float impact = length(cross(pos, eyevec));
    float along = dot(pos, eyevec);

    if (impact < ENTRY_R && along < 0.0) {
        float tEnter = -along - sqrt(max(0.0, ENTRY_R * ENTRY_R - impact * impact));
        float stepLen = FAR / float(ITERATIONS);
        float dither = fract(hash21(gl_FragCoord.xy));
        vec3 raypos = pos + eyevec * (tEnter + dither * stepLen);

        for (int i = 0; i < ITERATIONS; i++) {
            WarpSpace(eyevec, raypos);
            raypos += eyevec * stepLen;
            GasDisc(color, alpha, raypos, time, mainColor, eyevec);
            Haze(color, raypos, alpha, mainColor);

            float r2 = dot(raypos, raypos);
            captured = max(captured, 1.0 - smoothstep(0.32, 0.85, sqrt(r2)));
            if (alpha > 0.995) {
                break;
            }
            if (r2 > 49.0 && dot(raypos, eyevec) > 0.0) {
                break;
            }
        }
    }

    color *= EXPOSURE * (uIntensity * 100.0);
    float ring = pow(clamp(captured * (1.0 - captured) * 4.0, 0.0, 1.0), 6.0);
    color += mix(vec3(1.0), mainColor, 0.35) * ring * 0.6 * (1.0 - alpha);

    fragColor = vec4(clamp(color, 0.0, 1.0), uAlpha);
}
