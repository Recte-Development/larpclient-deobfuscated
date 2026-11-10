#version 330
// Adapted from MoonDLC 1.0.0 CC0 shader; four-iteration fluid field with bounded material response.
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

#define MAX_ITER 4

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0,
                0.0,   c,   s,
                0.0,  -s,   c);
}
mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(  c, 0.0,   s,
                0.0, 1.0, 0.0,
                 -s, 0.0,   c);
}

void main() {
    vec2 uv = gl_FragCoord.xy / uResolution.xy;
    vec2 sp = uv * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;

    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(-sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 rayW = rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV;

    // 3D noise driven directly by world direction - no spherical seams or poles.
    vec3 p = rayW * uScale;
    vec3 i = p;
    float c = 1.0;
    // The UI intensity is centered on 0.01; normalize it before field evaluation.
    float inten = max(uIntensity / 0.01, 0.1);

    for (int n = 0; n < MAX_ITER; n++) {
        float t = uTime * uSpeed * (1.0 - (3.0 / float(n + 1)));
        i = p + vec3(
            cos(t - i.x) + sin(t + i.y),
            sin(t - i.y) + cos(t + i.z),
            cos(t - i.z) + sin(t + i.x)
        );
        // Smooth absolute sine denominators avoid 0/0 at the world axes and
        // soften subpixel filaments without a camera-dependent texture projection.
        vec3 wave = vec3(sin(i.x + t), cos(i.y + t), sin(i.z + t));
        vec3 denominator = sqrt(wave * wave + vec3(0.0004));
        vec3 field = p * inten / denominator;
        c += inversesqrt(dot(field, field) + 0.0001);
    }

    c /= float(MAX_ITER);
    // Negative fourth-power lobes in the source produce huge clipped sheets.
    c = clamp(1.5 - sqrt(c), 0.0, 1.0);
    float brightness = c * c * c * c;

    vec3 color = uColor * (0.2 + 0.8 * brightness);

    fragColor = vec4(color, uAlpha);
}
