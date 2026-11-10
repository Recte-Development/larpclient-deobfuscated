#version 330
uniform sampler2D Source;
layout(std140) uniform ChamsParameters {
    vec4 Shape; vec4 Motion; vec4 Color1; vec4 Color2; vec4 Direction;
};
in vec2 texCoord;
out vec4 fragColor;
const float TAU = 6.28318530718;
void main() {
    vec2 texel = 1.0 / vec2(textureSize(Source, 0));
    vec4 center = texture(Source, texCoord);
    float neighbor = 0.0;
    vec3 neighborColor = vec3(0.0);
    for (int i = 0; i < 8; i++) {
        float angle = TAU * float(i) / 8.0;
        vec2 stepUv = vec2(cos(angle), sin(angle)) * texel * max(Shape.x, 1.0);
        vec4 a = texture(Source, texCoord + stepUv);
        vec4 b = texture(Source, texCoord + stepUv * 0.5);
        if (a.a > neighbor) { neighbor = a.a; neighborColor = a.rgb; }
        if (b.a > neighbor) { neighbor = b.a; neighborColor = b.rgb; }
    }
    float alpha = max((1.0 - center.a) * neighbor * Shape.w, center.a * Shape.z);
    float phase = (texCoord.x + (1.0 - texCoord.y)) * 0.5 * Shape.y - Motion.y;
    vec4 color = mix(Color1, Color2, 0.5 + 0.5 * sin(TAU * phase));
    if (Motion.x > 0.5) color = vec4(center.a > 0.0 ? center.rgb : neighborColor, 1.0);
    fragColor = alpha > 0.001 ? vec4(color.rgb, alpha * color.a) : vec4(0.0);
}
