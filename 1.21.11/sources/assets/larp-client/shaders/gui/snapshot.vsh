#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

// A piece of the ClickGUI exit snapshot. UV0 samples the copied frame; UV1 is the piece's original
// place relative to its window centre and UV2 the window's half size, both in 1/16 GUI pixels, and
// Position.z the window's corner radius, so the fragment can keep the rounded window outline.
in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV1;
in ivec2 UV2;

out vec2 texCoord0;
out vec4 vertexColor;
out vec2 localPosition;
flat out vec2 halfSize;
flat out float radius;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position.xy, 0.0, 1.0);
    texCoord0 = UV0;
    vertexColor = Color;
    localPosition = vec2(UV1) / 16.0;
    halfSize = vec2(UV2) / 16.0;
    radius = Position.z;
}
