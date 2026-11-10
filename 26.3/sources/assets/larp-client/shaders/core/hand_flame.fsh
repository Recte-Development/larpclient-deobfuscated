#version 150

uniform sampler2D Sampler0; // Scene + Hand
uniform sampler2D Sampler1; // Scene only
uniform sampler2D Sampler2; // Burning history: r = recent hand silhouettes, g = the hand this frame
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
#define CompositeMode TrailLagCompositeTime.z
#define Time TrailLagCompositeTime.w
#define CameraShift CameraShiftOuter.xy
#define FrameSeconds CameraShiftOuter.w
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
// Steps looked down the screen for something burning; more steps make taller flames smoother.
const int REACH_STEPS = 14;

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
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(p);
        p = p * 2.03 + vec2(17.1, 9.2);
        amplitude *= 0.5;
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

// How strongly a point feeds fire: the hand now fully, places it just left only faintly, so a swing or turn
// leaves wisps trailing behind the hand rather than a sheet of fire over everything it crossed.
float burning(vec2 uv, float climb) {
    vec2 history = texture(Sampler2, uv).rg;
    // Trail flames are also shorter, cooling twice as fast with height.
    return max(history.g * (1.0 - climb), history.r * 0.18 * (1.0 - climb) * (1.0 - climb));
}

// Fire colors by heat: deep red embers, orange body, yellow then near white at the hottest core.
vec3 naturalFire(float heat) {
    vec3 color = mix(vec3(0.45, 0.04, 0.0), vec3(1.0, 0.32, 0.03), smoothstep(0.0, 0.4, heat));
    color = mix(color, vec3(1.0, 0.72, 0.18), smoothstep(0.35, 0.7, heat));
    return mix(color, vec3(1.0, 0.96, 0.82), smoothstep(0.72, 1.0, heat));
}

// The same heat ramp built from a chosen color: dark at the edges, the color itself, a pale hot core.
vec3 coloredFire(vec3 color, float heat) {
    vec3 result = mix(color * 0.3, color, smoothstep(0.0, 0.45, heat));
    return mix(result, mix(color, vec3(1.0), 0.75), smoothstep(0.7, 1.0, heat));
}

void main() {
    if (CompositeMode < 0.5) {
        // --- History pass: where the hand has been burning recently ---
        // The camera spring shifts old silhouettes, so a turn leaves the fire trailing behind the hand.
        vec2 shift = CameraShift * (1.2 + Wobble * 0.4);
        vec4 previous = texture(Sampler2, safeUv(TexCoord + shift));
        float frames = clamp(FrameSeconds, 0.0, 0.1) * 60.0;
        // Decay is scaled by real frame time, so the trail lasts as long in seconds at any frame rate.
        // A fast hand sweeps a large area, so the burning trail fades quicker than the reference ghosting did.
        float keep = pow(clamp(0.88 + (FlameLength - 0.95) * 0.08, 0.72, 0.97), frames * 1.6);
        // The constant step clears 8-bit remainders that rounding would otherwise leave glowing forever.
        float trail = max(previous.r * keep - frames * 0.006, 0.0);
        float current = getHandMask(TexCoord);
        OutColor = vec4(max(trail, current), current, 0.0, 1.0);
        return;
    }

    // --- Composite: flames rising from everything that is burning ---
    float aspect = Resolution.x / Resolution.y;
    float speed = 1.0 + RiseSpeed * 1.5;
    float turbulence = max(Turbulence, 0.05);
    // Screen-height units, so flames keep their shape at any resolution or aspect ratio.
    vec2 p = vec2(TexCoord.x * aspect, TexCoord.y);
    float sway = fbm(p * vec2(4.0, 2.5) * turbulence - vec2(Time * 0.35, Time * speed * 0.9)) - 0.5;
    float tongues = fbm(p * vec2(13.0, 7.0) * turbulence - vec2(0.0, Time * speed * 2.2));
    float flicker = fbm(p * vec2(27.0, 15.0) * turbulence - vec2(Time * 0.2, Time * speed * 3.4));

    // Each column of fire has its own slowly changing height, so the outline breaks into separate tongues.
    float columns = fbm(vec2(p.x * 16.0 * turbulence + 3.7, Time * speed * 1.1 + p.y * 2.0));
    float height = 0.18 * FlameLength * (0.3 + smoothstep(0.3, 0.65, columns));
    // Sideways ripple that grows toward the tips, so tongue edges waver instead of rising straight.
    float warp = fbm(p * vec2(9.0, 3.5) * turbulence - vec2(0.0, Time * speed * 1.4)) - 0.47;
    float reach = 0.0;
    // A fixed per-pixel offset between steps turns the bands a step count leaves into fine, still grain.
    float jitter = hash(gl_FragCoord.xy);
    for (int i = 0; i <= REACH_STEPS; i++) {
        float f = min((float(i) + jitter) / float(REACH_STEPS), 1.0);
        // Higher parts of a flame bend further with the sway, so tongues curl rather than lean.
        vec2 below = TexCoord - vec2((sway * Wobble * 0.05 + warp * 0.1) * f / aspect, f * height);
        reach = max(reach, burning(safeUv(below), f));
    }

    // A thin burning edge all around the outline, so the fire hugs the sides and underside too.
    vec2 texel = 1.0 / Resolution;
    float radius = max(FlameWidth, 0.1) * Resolution.y * 0.008;
    float around = 0.0;
    for (int d = 0; d < 8; d++) {
        float angle = TAU * (float(d) + 0.5) / 8.0;
        around += burning(safeUv(TexCoord + vec2(cos(angle), sin(angle)) * texel * radius), 0.0);
    }
    around /= 8.0;

    vec4 here = texture(Sampler2, TexCoord);
    // Hottest at the source and cooling with height; noise eats ragged gaps into the cooler upper part.
    float rise = pow(reach, 0.7);
    float heat = rise * (0.65 + 0.7 * flicker) - (1.0 - rise) * (1.0 - tongues) * 0.35;
    heat = max(heat, around * (1.0 - here.g) * (0.35 + 0.45 * tongues));
    // Over the hand itself fire licks in only from the outline, so the hand stays readable inside its fire.
    float edge = clamp((1.0 - around) * 2.5, 0.0, 1.0);
    heat *= mix(1.0, edge * (0.5 + 0.5 * flicker), here.g);
    heat = clamp(heat * Fire * 1.25, 0.0, 1.0);

    float alpha = smoothstep(0.03, 0.4, heat) * clamp(Strength, 0.0, 2.0) * FlameColor.a;
    if (alpha <= 0.002) {
        OutColor = vec4(0.0);
        return;
    }
    vec3 tint = FlameColor.rgb;
    float saturation = (max(tint.r, max(tint.g, tint.b)) - min(tint.r, min(tint.g, tint.b))) / max(max(tint.r, max(tint.g, tint.b)), 0.001);
    // A white or grey color means plain fire; a saturated one recolors the whole ramp.
    vec3 rgb = mix(naturalFire(heat), coloredFire(tint, heat), smoothstep(0.15, 0.5, saturation));
    rgb *= Brightness * (0.94 + 0.06 * sin(Time * 3.5 + TexCoord.y * 10.0 + TexCoord.x * 5.0));
    alpha = clamp(alpha, 0.0, 1.0);
    // Premultiplied: fire covers less than it lights, so it keeps its color by day and glows at night.
    OutColor = vec4(clamp(rgb, 0.0, 1.5) * alpha, alpha * 0.8);
}
