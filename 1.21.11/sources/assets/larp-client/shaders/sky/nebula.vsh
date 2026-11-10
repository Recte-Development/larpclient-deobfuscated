#version 330
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
in vec3 Position;
out vec3 skyDirection;
void main() {
    skyDirection = Position;
    // Ignore camera translation. Pin to the far plane in Minecraft's OpenGL projection.
    vec4 clip = ProjMat * vec4(mat3(ModelViewMat) * Position, 1.0);
    gl_Position = vec4(clip.xy, clip.w, clip.w);
}
