#version 330
// Adapted from MoonDLC 1.0.0 CC0 shader; original field math retained, lattice hash replaced.
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

// Integer lattice hash (pcg3d, Jarzynski and Olano 2020). Every caller passes whole-number cells,
// so the value stays exact however far the drifting noise coordinates travel. The former
// fract(p * 443.9) hash ran out of float precision in long sessions and the sky turned blocky.
float hash(vec3 p) {
    uvec3 v = uvec3(ivec3(floor(p + 0.5))) * 1664525u + 1013904223u;
    v.x += v.y * v.z; v.y += v.z * v.x; v.z += v.x * v.y;
    v ^= v >> 16u;
    v.x += v.y * v.z; v.y += v.z * v.x; v.z += v.x * v.y;
    return float((v.x ^ v.y ^ v.z) >> 8u) * (1.0 / 16777216.0);
}

// Smooth value noise
float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float n000 = hash(i);
    float n100 = hash(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash(i + vec3(1.0, 1.0, 1.0));

    float nx00 = mix(n000, n100, f.x);
    float nx10 = mix(n010, n110, f.x);
    float nx01 = mix(n001, n101, f.x);
    float nx11 = mix(n011, n111, f.x);
    float nxy0 = mix(nx00, nx10, f.y);
    float nxy1 = mix(nx01, nx11, f.y);
    return mix(nxy0, nxy1, f.z);
}

// Fractal Brownian Motion - layered noise for gas cloud depth
float fbm(vec3 p) {
    float v = 0.0;
    float a = 0.5;
    vec3 shift = vec3(100.0);
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p = p * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main() {
    vec2 uv = gl_FragCoord.xy / uResolution.xy;
    vec2 sp = uv * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;

    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(-sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 rayW = rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV;

    float t = uTime * uSpeed * 0.15;
    vec3 p = rayW * uScale;

    // Swirling displacement
    float swirl = t * 0.4;
    mat3 rot = mat3(cos(swirl), 0.0, sin(swirl),
                    0.0,        1.0, 0.0,
                   -sin(swirl), 0.0, cos(swirl));
    p = rot * p;

    // Layer multiple fbm samples for volumetric depth
    float n1 = fbm(p + vec3(0.0, 0.0, t));
    float n2 = fbm(p * 0.7 + vec3(t * 0.3, -t * 0.2, 0.0));
    float n3 = fbm(p * 1.4 + vec3(-t * 0.15, t * 0.1, t * 0.25));

    float cloud = n1 * 0.5 + n2 * 0.35 + n3 * 0.15;

    // Ethereal glow with pow curve
    float glow = pow(cloud, 2.5) * 3.0;
    glow *= (uIntensity * 100.0);

    // Subtle color variation across the nebula
    vec3 color = uColor * glow;
    color += uColor * 0.6 * pow(n2, 3.0);
    color += uColor * vec3(0.8, 0.6, 1.0) * pow(n3, 4.0) * 0.4;

    fragColor = vec4(color, uAlpha);
}
