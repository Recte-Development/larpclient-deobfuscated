#version 330
uniform sampler2D Source;
uniform sampler2D Depth;
layout(std140) uniform MotionBlur {mat4 Reproject;vec4 Lens;vec4 Pixel;};
in vec2 texCoord;
out vec4 fragColor;
// Forward depth is cleared to 1.0 before the hand, so any written depth is the hand.
bool hand(vec2 uv){return texture(Depth,uv).r<1.0;}
void main(){
    vec4 source=texture(Source,texCoord);
    if(hand(texCoord)){fragColor=source;return;}
    vec2 ndc=texCoord*2.0-1.0;
    vec3 past=mat3(Reproject)*vec3(ndc*Lens.xy,-1.0);
    if(past.z>-1e-4){fragColor=source;return;}
    vec2 streak=(ndc-past.xy/(-past.z)/Lens.xy)*.5;
    float span=length(streak);
    if(span>Lens.z)streak*=Lens.z/span;
    if(length(streak/Pixel.xy)<1.0){fragColor=source;return;}
    int count=int(Lens.w);
    // Interleaved gradient noise turns sample banding into fine grain that reads as smooth.
    float jitter=fract(52.9829189*fract(dot(gl_FragCoord.xy,vec2(.06711056,.00583715))));
    vec3 sum=vec3(0.0);
    float total=0.0;
    for(int i=0;i<count;i++){
        float t=(float(i)+jitter)/float(count)-.5;
        vec2 uv=texCoord+streak*t;
        if(hand(uv))continue;
        // Centered soft shutter: streaks fade at both ends instead of stopping hard.
        float w=1.0-2.0*t*t;
        sum+=texture(Source,uv).rgb*w;
        total+=w;
    }
    fragColor=total>0.0?vec4(sum/total,source.a):source;
}
