#version 330
uniform sampler2D Before;
uniform sampler2D After;
uniform sampler2D HandDepth;
in vec2 texCoord;
out vec4 fragColor;
void main() {
    // 26.2 clears reversed hand depth to zero immediately before the wrapped hand draw.
    float opaque = step(1e-7, texture(HandDepth, texCoord).r);
    // Translucent item geometry can change color without writing hand depth.
    vec3 difference = abs(texture(After, texCoord).rgb - texture(Before, texCoord).rgb);
    float translucent = smoothstep(1.0 / 255.0, 3.0 / 255.0,
        max(difference.r, max(difference.g, difference.b)));
    fragColor = vec4(1.0, 1.0, 1.0, max(opaque, translucent));
}
