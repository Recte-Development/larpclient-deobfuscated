#version 330
uniform sampler2D Source;
uniform sampler2D Glow;
layout(std140) uniform ChamsParameters {
    vec4 Shape; vec4 Motion; vec4 Color1; vec4 Color2; vec4 Direction;
};
in vec2 texCoord;
out vec4 fragColor;
void main() {
    vec4 edge = texture(Source, texCoord);
    vec4 glow = texture(Glow, texCoord);
    float halo = pow(clamp(glow.a, 0.0, 1.0), 0.6) * Motion.w;
    float alpha = edge.a + halo * (1.0 - edge.a);
    vec3 color = alpha > 0.001
        ? (edge.rgb * edge.a + glow.rgb * halo * (1.0 - edge.a)) / alpha : vec3(0.0);
    fragColor = vec4(color, alpha > 0.001 ? alpha : 0.0);
}
