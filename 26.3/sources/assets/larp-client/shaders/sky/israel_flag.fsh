#version 330
layout(std140) uniform SkyPalette {
    vec4 FlagParameters; // rotation, exposure, horizon blend, reserved
};
in vec3 skyDirection;
out vec4 fragColor;

float segmentDistance(vec2 p, vec2 a, vec2 b) {
    vec2 edge = b - a;
    return length(p - a - edge * clamp(dot(p - a, edge) / dot(edge, edge), 0.0, 1.0));
}

float band(float height, float center, float halfWidth) {
    float aa = max(fwidth(height), 0.003);
    return 1.0 - smoothstep(halfWidth - aa, halfWidth + aa, abs(height - center));
}

void main() {
    vec3 d = normalize(skyDirection);
    float c = cos(FlagParameters.x), s = sin(FlagParameters.x);
    // Direction-space artwork is continuous across every sky cube face and azimuth seam.
    vec2 p = mat2(c, -s, s, c) * d.xz;
    vec2 top = vec2(0.0, 0.32);
    vec2 left = vec2(-0.27712813, -0.16);
    vec2 right = vec2(0.27712813, -0.16);
    float edge = min(segmentDistance(p, top, left),
                 min(segmentDistance(p, left, right), segmentDistance(p, right, top)));
    edge = min(edge, min(segmentDistance(p, -top, -left),
                    min(segmentDistance(p, -left, -right), segmentDistance(p, -right, -top))));
    float aa = max(length(fwidth(p)), 0.001);
    // A single outlined symbol at the zenith; no repeated panels or mirrored lower symbol.
    float star = (1.0 - smoothstep(0.013 - aa, 0.013 + aa, edge))
               * smoothstep(0.80, 0.90, d.y);
    float stripes = max(band(d.y, 0.22, 0.032), band(d.y, 0.55, 0.032));
    vec3 white = mix(vec3(0.97, 0.98, 1.0), vec3(0.90, 0.94, 0.99),
                     smoothstep(0.0, 1.0, d.y));
    vec3 blue = vec3(0.045, 0.24, 0.68);
    vec3 color = mix(white, blue, max(stripes * 0.85, star));
    if (FlagParameters.z > 0.5) {
        color = mix(vec3(1.0), color, smoothstep(-0.03, 0.12, d.y));
    }
    fragColor = vec4(clamp(color * FlagParameters.y, 0.0, 1.0), 1.0);
}
