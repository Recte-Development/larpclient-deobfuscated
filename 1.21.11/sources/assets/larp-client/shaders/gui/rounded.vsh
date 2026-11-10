#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
#ifdef LARP_UI_SWEEP
in ivec2 UV2;
flat out vec2 sweep;
#endif
#ifdef LARP_UI_SIZED
// World Menu panels split and cut shapes, so the size travels with every vertex in 1/64 radius.
in ivec2 UV1;
#endif
#ifdef LARP_UI_RIPPLE
// A ripple's circle: centre in UV1 and radius in UV2.x, in 1/64 radius like the shape's own units.
in ivec2 UV1;
in ivec2 UV2;
flat out vec3 ripple;
#endif

out vec2 localPosition;
flat out vec2 halfSize;
flat out float padding;
out vec4 vertexColor;

void main() {
    // Position.z is normalized coverage padding, not projected geometry depth.
    gl_Position = ProjMat * ModelViewMat * vec4(Position.xy, 0.0, 1.0);
    localPosition = UV0;
#ifdef LARP_UI_SIZED
    halfSize = vec2(UV1) / 64.0;
#else
    halfSize = abs(UV0) - Position.z;
#endif
    padding = Position.z;
#ifdef LARP_UI_RIPPLE
    ripple = vec3(vec2(UV1) / 64.0, float(UV2.x) / 64.0);
#endif
    vertexColor = Color;
#ifdef LARP_UI_SWEEP
    // The lightmap slot carries the light's place on the contour in 1/32768 laps and its outside glow.
    sweep = vec2(float(UV2.x) / 32768.0, float(UV2.y));
#endif
}
