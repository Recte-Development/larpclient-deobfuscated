#version 330
in vec3 Position;
out vec3 skyDirection;
layout(std140) uniform SkyPalette {
    vec4 LarpTimeResolution;
    vec4 LarpColorAlpha;
    vec4 LarpMotion;
    vec4 LarpCamera;
    vec4 LarpUnused0; vec4 LarpUnused1; vec4 LarpUnused2;
    vec4 LarpUnused3; vec4 LarpUnused4; vec4 LarpUnused5;
};
mat3 larpRotateX(float angle) {
    float c = cos(angle), s = sin(angle);
    return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c);
}
mat3 larpRotateY(float angle) {
    float c = cos(angle), s = sin(angle);
    return mat3(c, 0.0, s, 0.0, 1.0, 0.0, -s, 0.0, c);
}
void main() {
    // A single oversized triangle covers every pixel once. Native world projection cannot clip it.
    gl_Position = vec4(Position.xy, 0.0, 1.0);
    float tanV = tan(radians(LarpCamera.z) * 0.5);
    float aspect = LarpTimeResolution.y / max(1.0, LarpTimeResolution.z);
    vec3 ray = vec3(-Position.x * tanV * aspect, Position.y * tanV, 1.0);
    // Native camera screen-right points toward -X at yaw zero, while forward points toward +Z.
    // Core and post effects use the same unnormalized world ray; fragment stages normalize it.
    skyDirection = larpRotateY(LarpCamera.x) * larpRotateX(LarpCamera.y) * ray;
}
