#version 330

#moj_import <minecraft:dynamictransforms.glsl>

in vec2 localPosition;
flat in vec2 halfSize;
flat in float padding;
in vec4 vertexColor;
#ifdef LARP_UI_SWEEP
flat in vec2 sweep;
#endif
#ifdef LARP_UI_RIPPLE
flat in vec3 ripple;
#endif

out vec4 fragColor;

#ifndef LARP_UI_RIM_WIDTH
#define LARP_UI_RIM_WIDTH 1.0
#endif

#ifdef LARP_UI_SWEEP
const float PI = 3.14159265;
// Laps of contour covered by the light's tail and by its short leading edge, the stroke in physical
// pixels, and the strength of its haze outside and just inside the edge.
const float SWEEP_TAIL = 0.32;
const float SWEEP_HEAD = 0.012;
const float SWEEP_STROKE = 1.5;
const float SWEEP_GLOW = 0.55;
const float SWEEP_INNER = 0.2;

// Contour length, clockwise from the left end of the top edge, to the edge point nearest p.
float contourPosition(vec2 p, vec2 inner) {
    vec2 corner = clamp(p, -inner, inner);
    vec2 away = p - corner;
    if (away == vec2(0.0)) {
        // Inside the straight core, the nearer side owns the point.
        vec2 gap = inner - abs(p);
        if (gap.x < gap.y) { away.x = p.x < 0.0 ? -1.0 : 1.0; corner.x = away.x * inner.x; }
        else { away.y = p.y < 0.0 ? -1.0 : 1.0; corner.y = away.y * inner.y; }
    }
    float angle = atan(away.y, away.x);
    // The right half runs down from the top edge; the left half runs back up from the bottom edge.
    if (angle >= -0.5 * PI && angle < 0.5 * PI) return corner.x + inner.x + corner.y + inner.y + angle + 0.5 * PI;
    if (angle < 0.0) angle += 2.0 * PI;
    return 2.0 * (inner.x + inner.y) + PI + inner.x - corner.x + inner.y - corner.y + angle - 0.5 * PI;
}
#endif

void main() {
    // UV units are corner radii, so a single pipeline serves every size and radius.
    vec2 q = abs(localPosition) - halfSize + 1.0;
    float distance = min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - 1.0;
    float footprint = max(fwidth(distance), 0.00001);
#if defined(LARP_UI_SWEEP)
    // A light travels the contour: a crisp stroke on the edge with a soft haze, brightest at its head
    // and fading along the tail it leaves behind.
    if (distance < -padding - 2.0 * footprint) discard;
    vec2 inner = max(halfSize - 1.0, 0.0);
    float lap = 4.0 * (inner.x + inner.y) + 2.0 * PI;
    float behind = fract(sweep.x - contourPosition(localPosition, inner) / lap);
    float tail = max(1.0 - behind / SWEEP_TAIL, 0.0);
    float lead = (1.0 - behind) / SWEEP_HEAD;
    float light = max(tail * tail, exp(-lead * lead));
    float stroke = clamp(0.5 - distance / footprint, 0.0, 1.0)
        - clamp(0.5 - SWEEP_STROKE - distance / footprint, 0.0, 1.0);
    // The haze reaches the full padding outward and half of it inward, and follows the Bloom setting.
    float t = clamp(distance > 0.0 ? distance / padding : -2.0 * distance / padding, 0.0, 1.0);
    float haze = sweep.y * exp(-4.8 * t * t) * (distance > 0.0 ? SWEEP_GLOW : SWEEP_INNER);
    float coverage = light * min(1.0, stroke + haze);
#elif defined(LARP_UI_GLOW)
    // Outside-only falloff across the padding, so a translucent surface is never darkened from below.
    float t = clamp(distance / max(padding, 0.00001), 0.0, 1.0);
    float coverage = exp(-4.8 * t * t) * clamp(0.5 + distance / footprint, 0.0, 1.0);
#else
    float coverage = clamp(0.5 - distance / footprint, 0.0, 1.0);
#endif
#ifdef LARP_UI_RIPPLE
    // The ripple's circle, kept inside the rounded outline above.
    float circle = length(localPosition - ripple.xy) - ripple.z;
    coverage *= clamp(0.5 - circle / max(fwidth(circle), 0.00001), 0.0, 1.0);
#endif
#ifdef LARP_UI_RIM
    // Subtract the inset coverage to keep a contour of fixed pixel width at every scale.
    coverage -= clamp(0.5 - LARP_UI_RIM_WIDTH - distance / footprint, 0.0, 1.0);
#endif
    vec4 color = vertexColor * ColorModulator;
#ifdef LARP_UI_SWEEP
    // The head of the stroke burns a little whiter than the accent it leaves behind.
    color.rgb = mix(color.rgb, vec3(1.0), 0.35 * stroke * pow(light, 6.0));
#endif
    color.a *= coverage;
    if (color.a == 0.0) discard;
    fragColor = color;
}
