#version 330
uniform sampler2D Source;
layout(std140) uniform ChamsParameters {
    vec4 Shape; vec4 Motion; vec4 Color1; vec4 Color2; vec4 Direction;
};
in vec2 texCoord;
out vec4 fragColor;
void main() {
    vec2 stepUv = Direction.xy / vec2(textureSize(Source, 0));
    float radius = clamp(Motion.z, 1.0, 32.0);
    float sigma = radius * 0.5;
    vec3 rgb = vec3(0.0);
    float alphaSum = 0.0, weightSum = 0.0;
    for (int i = -32; i <= 32; i++) {
        float x = float(i);
        if (abs(x) > radius) continue;
        float weight = exp(-x * x / (2.0 * sigma * sigma));
        vec4 sampleColor = texture(Source, texCoord + stepUv * x);
        rgb += sampleColor.rgb * sampleColor.a * weight;
        alphaSum += sampleColor.a * weight;
        weightSum += weight;
    }
    fragColor = vec4(alphaSum > 1e-5 ? rgb / alphaSum : vec3(0.0), alphaSum / weightSum);
}
