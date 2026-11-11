#version 330
uniform sampler2D Source;
uniform sampler2D Mask;
uniform sampler2D SceneDepth;
layout(std140) uniform Refraction { vec4 Displacement; };
in vec2 texCoord;
out vec4 fragColor;
void main() {
    vec4 m = texture(Mask, texCoord);
    vec4 original = texture(Source, texCoord);
    if (m.a < .001) { fragColor = original; return; }
    vec2 offset = (m.rg * 2.0 - 1.0) * Displacement.xy * m.a;
    vec2 sampleUv = clamp(texCoord + offset, vec2(0.0), vec2(1.0));
    // Reversed depth: a nearer foreground surface must never be pulled through the effect.
    float sampledDepth = texture(SceneDepth, sampleUv).r;
    if (sampledDepth > m.b + max(.00001, m.b * .012)) sampleUv = texCoord;
    fragColor = vec4(mix(original.rgb, texture(Source, sampleUv).rgb, m.a), original.a);
}
