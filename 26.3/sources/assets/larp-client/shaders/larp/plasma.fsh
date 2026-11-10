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

#define PI 3.14159265359

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

    float t = uTime * uSpeed * 0.8;
    vec3 p = rayW * uScale;

    // Overlapping multi-frequency sine wave plasma fields in 3D
    float v1 = sin(p.x * 1.5 + t);
    float v2 = sin(p.y * 1.5 + t * 1.2);
    float v3 = sin(p.z * 1.5 + t * 0.9);
    float v4 = sin(length(p) + t * 1.5);

    float plasma = v1 + v2 + v3 + v4;
    plasma = sin(plasma * 0.75 * PI) * 0.5 + 0.5;

    // Glowing organic plasma color blending
    vec3 color = uColor * (0.3 + 0.7 * plasma);
    color += vec3(sin(plasma * PI), cos(plasma * PI), sin(t)) * 0.15;
    color *= (uIntensity * 100.0);

    fragColor = vec4(color, uAlpha);
}
