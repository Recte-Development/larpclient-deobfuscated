#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;
uniform sampler2D Sampler4;

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

float colorMaskAt(vec2 uv) {
    if (uv.x <= 0.0 || uv.y <= 0.0 || uv.x >= 1.0 || uv.y >= 1.0) {
        return 0.0;
    }

    vec3 afterHand = texture(Sampler0, uv).rgb;
    vec3 beforeHand = texture(Sampler1, uv).rgb;
    float delta = length(afterHand - beforeHand);
    float brightness = max(afterHand.r, max(afterHand.g, afterHand.b));
    float colorMask = smoothstep(0.014, 0.090, delta);
    float visibleMask = smoothstep(0.012, 0.060, brightness);
    return colorMask * visibleMask;
}

float depthMaskAt(vec2 uv) {
    if (uv.x <= 0.0 || uv.y <= 0.0 || uv.x >= 1.0 || uv.y >= 1.0) {
        return 0.0;
    }

    float afterDepth = texture(Sampler3, uv).r;
    float beforeDepth = texture(Sampler4, uv).r;
    float closerDelta = beforeDepth - afterDepth;
    return smoothstep(0.000015, 0.0012, closerDelta);
}

float handMaskAt(vec2 uv) {
    return max(colorMaskAt(uv), depthMaskAt(uv));
}

float blurredMask(vec2 uv) {
    vec2 texel = 1.0 / Resolution;
    float radiusPx = max(Radius, 1.0);
    float mask = colorMaskAt(uv) * 1.15;
    float weight = 1.25;

    for (int d = 0; d < 10; d++) {
        float angle = TAU * (float(d) / 10.0);
        vec2 dir = vec2(cos(angle), sin(angle));
        for (int s = 1; s <= 3; s++) {
            float progress = float(s) / 3.0;
            float w = 1.0 - progress * 0.55;
            mask += colorMaskAt(uv + dir * texel * radiusPx * progress) * w;
            weight += w;
        }
    }

    return clamp(mask / weight, 0.0, 1.0);
}

void main() {
    if (CompositeMode < 0.5) {
        vec2 laggedShift = CameraShift * (0.85 + Lag * 0.05);
        vec4 previous = texture(Sampler2, safeUv(TexCoord + laggedShift)) * TrailDecay;
        float centerMask = handMaskAt(TexCoord);
        float glowMask = blurredMask(TexCoord);
        if (OuterOnly > 0.5) {
            previous *= 1.0 - centerMask * 0.98;
        }

        float outsideMask = smoothstep(0.010, 0.30, max(glowMask - centerMask * 0.99, 0.0));
        float sourceMask = OuterOnly > 0.5 ? outsideMask : glowMask * 0.92 + centerMask * 0.35;
        float fresh = clamp(sourceMask * Strength, 0.0, 1.0);

        vec3 handColor = texture(Sampler0, TexCoord).rgb;
        vec3 sourceColor = OuterOnly > 0.5 ? EchoColor.rgb : mix(EchoColor.rgb, handColor, 0.12);
        vec3 echoRgb = sourceColor * fresh;
        float echoAlpha = fresh * EchoColor.a * (OuterOnly > 0.5 ? 0.92 : 1.0);

        vec3 rgb = previous.rgb + echoRgb * (1.0 - previous.a * 0.28);
        float alpha = previous.a + echoAlpha * (1.0 - previous.a * 0.22);
        OutColor = vec4(clamp(rgb, 0.0, 1.0), clamp(alpha, 0.0, 1.0));
        return;
    }

    vec4 echo = texture(Sampler2, TexCoord);
    if (OuterOnly > 0.5) {
        echo *= 1.0 - handMaskAt(TexCoord);
    }
    float shimmer = 0.94 + 0.06 * sin(Time * 2.5 + TexCoord.y * 10.0 + TexCoord.x * 4.0);
    OutColor = vec4(clamp(echo.rgb * shimmer, 0.0, 1.0), clamp(echo.a, 0.0, 1.0));
}
