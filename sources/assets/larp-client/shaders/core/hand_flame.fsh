#version 150

uniform sampler2D Sampler0; // Scene + Hand
uniform sampler2D Sampler1; // Scene only
uniform sampler2D Sampler2; // Previous Trail History
uniform sampler2D Sampler3; // Hand Depth
uniform sampler2D Sampler4; // Scene Depth

layout(std140) uniform EchoParameters {
    vec4 ResolutionStrengthRadius;
    vec4 TrailLagCompositeTime;
    vec4 CameraShiftOuter;
    vec4 MaterialColor;
    vec4 FlameMotion;
    vec4 FlameShape;
};
#define Resolution ResolutionStrengthRadius.xy
#define Strength ResolutionStrengthRadius.z
#define Radius ResolutionStrengthRadius.w
#define TrailDecay TrailLagCompositeTime.x
#define Lag TrailLagCompositeTime.y
#define CompositeMode TrailLagCompositeTime.z
#define Time TrailLagCompositeTime.w
#define CameraShift CameraShiftOuter.xy
#define OuterOnly CameraShiftOuter.z
#define EchoColor MaterialColor
#define FlameColor MaterialColor
#define RiseSpeed FlameMotion.x
#define Wobble FlameMotion.y
#define FlameLength FlameMotion.z
#define Brightness FlameMotion.w
#define FlameWidth FlameShape.x
#define Fire FlameShape.y
#define Turbulence FlameShape.z

in vec2 TexCoord;
out vec4 OutColor;

const float TAU = 6.28318530718;

vec2 safeUv(vec2 uv) {
    return clamp(uv, vec2(0.001), vec2(0.999));
}

float hash(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(0.1031, 0.1030, 0.0973));
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(p * frequency);
        amplitude *= 0.5;
        frequency *= 2.0;
    }
    return value;
}

float getHandMask(vec2 uv) {
    if (uv.x <= 0.0 || uv.y <= 0.0 || uv.x >= 1.0 || uv.y >= 1.0) {
        return 0.0;
    }

    vec3 afterHand = texture(Sampler0, uv).rgb;
    vec3 beforeHand = texture(Sampler1, uv).rgb;
    float colorDiff = length(afterHand - beforeHand);

    float afterDepth = texture(Sampler3, uv).r;
    float beforeDepth = texture(Sampler4, uv).r;
    float depthDiff = max(0.0, afterDepth - beforeDepth);

    float colorMask = smoothstep(0.015, 0.075, colorDiff);
    float depthMask = smoothstep(0.00002, 0.0012, depthDiff);

    return max(colorMask, depthMask);
}

float getGlowMask(vec2 uv) {
    vec2 texel = 1.0 / Resolution;
    float radiusPx = max(FlameWidth * 7.0, 1.0);
    float centerMask = getHandMask(uv);
    float sum = centerMask * 2.0;
    float weight = 2.0;

    for (int d = 0; d < 8; d++) {
        float angle = TAU * (float(d) / 8.0);
        vec2 dir = vec2(cos(angle), sin(angle));
        float m = getHandMask(uv + dir * texel * radiusPx);
        sum += m;
        weight += 1.0;
    }

    return clamp(sum / weight, 0.0, 1.0);
}

void main() {
    if (CompositeMode < 0.5) {
        // --- Accumulation Pass: Create smooth motion trail ghosting ---
        vec2 shift = CameraShift * (1.2 + Wobble * 0.4);
        vec2 rise = vec2(0.0, -RiseSpeed * 0.006);
        vec2 prevUv = safeUv(TexCoord + shift + rise);

        vec4 prevTrail = texture(Sampler2, prevUv);

        // Smooth decay over time for persistent motion trail ghosting
        float decay = 0.88 + (FlameLength - 0.95) * 0.08;
        decay = clamp(decay, 0.72, 0.97);
        prevTrail *= decay;

        // Current item flame injection
        float currentMask = getGlowMask(TexCoord);

        // Add fire noise turbulence
        vec2 noiseUv = TexCoord * vec2(3.5, 5.0) * Turbulence;
        noiseUv.y -= Time * (1.2 + RiseSpeed * 0.8);
        noiseUv.x += sin(Time * 2.5 + TexCoord.y * 6.0) * Wobble * 0.12;
        float fireNoise = fbm(noiseUv);

        float intensity = currentMask * (0.65 + 0.35 * fireNoise) * Fire * Strength;
        intensity = clamp(intensity, 0.0, 1.0);

        vec3 flameRgb = FlameColor.rgb * intensity * Brightness;
        float flameAlpha = intensity * FlameColor.a;

        vec4 currentFlame = vec4(flameRgb, flameAlpha);

        // Max blend preserves historical ghosting trails while layering current flame
        vec3 accumRgb = max(prevTrail.rgb, currentFlame.rgb);
        float accumAlpha = max(prevTrail.a, currentFlame.a);

        OutColor = vec4(clamp(accumRgb, 0.0, 1.0), clamp(accumAlpha, 0.0, 1.0));
        return;
    }

    // --- Composite Pass: Render trail smoothly onto screen ---
    vec4 trail = texture(Sampler2, TexCoord);
    float handMask = getHandMask(TexCoord);

    // Render motion trail behind and around item with smooth glowing bloom
    float shimmer = 0.94 + 0.06 * sin(Time * 3.5 + TexCoord.y * 10.0 + TexCoord.x * 5.0);
    vec3 compositeRgb = trail.rgb * shimmer;
    float compositeAlpha = trail.a;

    OutColor = vec4(clamp(compositeRgb, 0.0, 1.0), clamp(compositeAlpha, 0.0, 1.0));
}
