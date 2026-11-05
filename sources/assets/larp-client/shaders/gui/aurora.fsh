#version 330

// ClickGUI backdrops, chosen by Shape.z. Aurora (0): four accent-derived light fields drift over a
// slowly warped plane with a faint ribbon of light. Nebula (1): folding clouds shaded from the
// secondary color into the accent, with a few faint twinkling points. Waves (2): ribbons of light
// flowing across the screen. Each settles into a vignette toward the centred window.
// Output is straight alpha; Base.a is how much of the scene behind the GUI is covered.
layout(std140) uniform AuroraParameters {
    vec4 View;    // width, height, seconds
    vec4 ColorA;  // rgb, weight
    vec4 ColorB;
    vec4 ColorC;
    vec4 ColorD;
    vec4 Base;    // rgb base tone, coverage
    vec4 Shape;   // light strength, vignette, style
};

in vec2 texCoord;
out vec4 fragColor;

float hash(vec2 p) {
    p = fract(p * vec2(233.34, 851.73));
    p += dot(p, p + 23.45);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(p);
        p = mat2(0.8, -0.6, 0.6, 0.8) * p * 2.02 + vec2(3.1, 1.7);
        amplitude *= 0.5;
    }
    return value;
}

float lightField(vec2 p, vec2 centre, float falloff) {
    vec2 d = p - centre;
    return exp(-dot(d, d) * falloff);
}

vec3 nebula(vec2 p, float t) {
    vec2 n = p * 1.6 + vec2(t * 0.018, -t * 0.012);
    vec2 drift = vec2(fbm(n + vec2(1.7, 9.2)), fbm(n + vec2(8.3, 2.8)));
    float cloud = fbm(n + drift * 1.8 + vec2(0.0, t * 0.02));
    float density = smoothstep(0.22, 0.78, cloud);
    vec3 light = mix(ColorB.rgb * ColorB.a, ColorA.rgb * ColorA.a, smoothstep(0.4, 0.75, cloud)) * density * 3.2;
    // Sparse round points that brighten and fade on their own clocks.
    vec2 grid = texCoord * View.xy / 3.0;
    vec2 cell = floor(grid);
    float seed = hash(cell);
    float twinkle = step(0.994, seed) * (0.5 + 0.5 * sin(t * 1.3 + hash(cell + 7.0) * 6.2832))
                  * smoothstep(0.5, 0.1, length(fract(grid) - 0.5));
    return light + vec3(twinkle * 0.5);
}

vec3 waves(vec2 p, float t) {
    vec3 light = vec3(0.0);
    for (int i = 0; i < 4; i++) {
        float fi = float(i);
        float centre = -0.26 + fi * 0.17
                     + 0.07 * sin(p.x * (2.2 + fi * 0.6) + t * (0.35 + fi * 0.08) + fi * 1.3)
                     + 0.035 * sin(p.x * 5.1 - t * 0.5 + fi * 2.1);
        float d = p.y - centre;
        float band = exp(-d * d * 900.0 / (1.0 + fi)) * 0.9 + exp(-d * d * 60.0) * 0.22;
        light += mix(ColorB.rgb * ColorB.a, ColorA.rgb * ColorA.a, fi / 3.0) * band;
    }
    return light * 1.4;
}

// The dark base covers Base.a of the scene and the light adds on top, so the light keeps its
// strength at any dim. Alpha only rises above the coverage where the light would overflow; at the
// former 0.55 coverage this is the earlier aurora exactly.
vec4 compose(vec3 light, vec2 p, float aspect) {
    vec3 color = Base.rgb * Base.a + max(light, vec3(0.0)) * Shape.x * 0.55;
    float vignette = smoothstep(1.2, 0.25, length(vec2(p.x / aspect, p.y)) * 1.3);
    color *= mix(1.0, vignette, Shape.y);
    float alpha = max(Base.a, max(color.r, max(color.g, color.b)));
    return alpha > 0.0 ? vec4(color / alpha, alpha) : vec4(0.0);
}

void main() {
    float aspect = View.x / max(View.y, 1.0);
    vec2 p = vec2((texCoord.x - 0.5) * aspect, texCoord.y - 0.5);
    float t = View.z;

    if (Shape.z > 0.5) {
        fragColor = compose(Shape.z > 1.5 ? waves(p, t) : nebula(p, t), p, aspect);
        return;
    }

    vec2 warp = vec2(fbm(p * 1.5 + vec2(0.0, t * 0.03)), fbm(p * 1.5 + vec2(5.2, -t * 0.027))) - 0.5;
    vec2 q = p + warp * 0.5;

    vec2 a = vec2(-0.40 * aspect + 0.10 * sin(t * 0.100), -0.22 + 0.08 * cos(t * 0.120));
    vec2 b = vec2( 0.38 * aspect + 0.09 * cos(t * 0.085), -0.24 + 0.10 * sin(t * 0.110));
    vec2 c = vec2(-0.28 * aspect + 0.12 * cos(t * 0.070),  0.32 + 0.07 * sin(t * 0.095));
    vec2 d = vec2( 0.32 * aspect + 0.08 * sin(t * 0.075),  0.28 + 0.09 * cos(t * 0.105));

    vec3 light = ColorA.rgb * lightField(q, a, 2.5) * ColorA.a
               + ColorB.rgb * lightField(q, b, 2.3) * ColorB.a
               + ColorC.rgb * lightField(q, c, 2.8) * ColorC.a
               + ColorD.rgb * lightField(q, d, 3.0) * ColorD.a;

    float bend = fbm(q * 2.1 + vec2(t * 0.045, 0.0));
    float ribbon = pow(max(sin((q.y + bend * 0.5) * 8.5 + t * 0.22), 0.0), 7.0)
                 * smoothstep(0.6, 0.0, abs(q.y + 0.04));
    light += mix(ColorA.rgb, ColorB.rgb, texCoord.x) * ribbon * 0.3;

    fragColor = compose(light, p, aspect);
}
