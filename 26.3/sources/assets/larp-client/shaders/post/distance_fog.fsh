#version 330
uniform sampler2D Depth;
// Depth before translucent terrain, read only by the translucent surfaces step.
uniform sampler2D OpaqueDepth;
// Camera: height above sea level, 1/fog height, zero-to-one depth flag, step (1 fogs only pixels
// whose depth changed since translucent terrain began; 0 fogs everything). Range: start, end,
// density, sine of the sky band height. Tint.a is sun glow; Sun.w is how visible the sun is.
layout(std140) uniform DistanceFog {mat4 InverseViewProjection;vec4 Camera;vec4 Range;vec4 Tint;vec4 Sun;};
in vec2 texCoord;
out vec4 fragColor;

vec3 relative(vec2 uv, float depth) {
    float z = Camera.z > .5 ? depth : depth * 2.0 - 1.0;
    vec4 p = InverseViewProjection * vec4(uv * 2.0 - 1.0, z, 1.0);
    return p.xyz / max(abs(p.w), 1e-7) * sign(p.w);
}

// Mean of the height density along the ray: full below sea level, exp(-h / height) above it.
// Mountain tops and high ground rise out of the fog while valleys and the sea stay hazy.
float meanDensity(float from, float to) {
    float k = Camera.y, low = min(from, to), high = max(from, to);
    if (high <= 0.0) return 1.0;
    float span = high - low;
    float below = span > 1e-3 ? clamp(-low / span, 0.0, 1.0) : 0.0;
    float a = max(low, 0.0), d = high - a;
    float above = d * k < 1e-3 ? exp(-k * a) : (exp(-k * a) - exp(-k * high)) / (k * d);
    return below + (1.0 - below) * above;
}

void main() {
    float depth = texture(Depth, texCoord).r;
    // The early step already fogged everything behind glass, water and ice from the opaque depth.
    if (Camera.w > .5 && depth == texture(OpaqueDepth, texCoord).r) discard;
    // Reversed depth is cleared to zero: nothing was drawn there, so it is sky.
    bool sky = depth <= 0.0;
    vec3 ray = relative(texCoord, sky ? .5 : depth);
    float travel = length(ray);
    vec3 direction = ray / max(travel, 1e-4);
    float fog;
    if (sky) {
        // A low band over the horizon only; the sky above it stays clear.
        fog = Range.w > 0.0 ? Range.z * (1.0 - smoothstep(0.0, Range.w, direction.y)) : 0.0;
    } else {
        float t = clamp((travel - Range.x) / max(Range.y - Range.x, 1.0), 0.0, 1.0);
        // Eases in from the start distance and settles at full density by the end distance.
        float shape = (1.0 - exp(-3.0 * t * t)) / (1.0 - exp(-3.0));
        fog = Range.z * shape * meanDensity(Camera.x, Camera.x + ray.y);
    }
    float facing = max(dot(direction, Sun.xyz), 0.0);
    float glow = Tint.a * Sun.w * (pow(facing, 6.0) * .55 + pow(facing, 48.0) * .45);
    vec3 warm = min(Tint.rgb * vec3(1.45, 1.2, .9) + vec3(.08, .05, 0.0), vec3(1.0));
    vec3 color = mix(Tint.rgb, warm, clamp(glow, 0.0, 1.0));
    // Interleaved gradient noise hides 8-bit banding in wide, slow gradients.
    float noise = fract(52.9829189 * fract(dot(gl_FragCoord.xy, vec2(.06711056, .00583715))));
    fog = clamp(fog + (noise - .5) / 255.0, 0.0, 1.0);
    fragColor = vec4(color, fog);
}
