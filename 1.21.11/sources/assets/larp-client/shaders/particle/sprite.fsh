#version 330
uniform sampler2D Sampler0;
#ifdef SOFT_PARTICLE
#moj_import <minecraft:projection.glsl>
uniform sampler2D SceneDepth;
layout(std140) uniform ParticleDepth { vec4 DepthParams; };
float viewDistance(float d) {
    float ndc = d * DepthParams.x + DepthParams.y;
    return abs(ProjMat[3][2] / (ndc + ProjMat[2][2]));
}
#endif
in vec2 texCoord0;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec4 c=texture(Sampler0,texCoord0)*vertexColor;
#ifdef DITHER_FADE
    // UV-anchored cutout fade keeps the textured character depth-writing and self-occluding.
    vec2 cell=floor(texCoord0*512.0);
    float noise=fract(sin(dot(cell,vec2(12.9898,78.233)))*43758.5453);
    if(noise>=vertexColor.a) discard;
    c.a=1.0;
#endif
#ifdef SOFT_PARTICLE
    float scene = texelFetch(SceneDepth, ivec2(gl_FragCoord.xy), 0).r;
    // Standard OpenGL depth clears at one. Empty sky has no intersection to soften.
    if(scene < 1.0) c.a *= clamp((viewDistance(scene) - viewDistance(gl_FragCoord.z)) / DepthParams.z, 0.0, 1.0);
#endif
    if(c.a<0.002) discard;
#ifdef PREMULTIPLIED
    c.rgb*=c.a;
#endif
    fragColor=c;
}
