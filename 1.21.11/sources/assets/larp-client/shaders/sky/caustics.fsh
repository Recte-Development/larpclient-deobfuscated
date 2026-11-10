#version 330
#moj_import <larp-client:sky_common.glsl>
float caustic(vec3 p, float t) {
    vec3 warp=vec3(sin(p.y*1.3+t)+cos(p.z*.8-t*.7),
                   sin(p.z*1.1-t*.8)+cos(p.x+t*.5),
                   sin(p.x*.9+t*.6)+cos(p.y*1.2-t));
    p += warp*Effect.z*.42;
    float field=sin(p.x+sin(p.y+t)*.8)+sin(p.y+sin(p.z-t*.6)*.8)+sin(p.z+sin(p.x+t*.7)*.8);
    float width=max(fwidth(field)*1.2,.035);
    return 1.0-smoothstep(.04,.10+width,abs(field));
}
void main() {
    vec3 d=direction();
    float t=Motion.x*.13;
    vec3 p=d*Effect.x*2.0;
    float light=caustic(p,t)*.65+caustic(p.yzx*1.37+5.7,-t*.67)*.35;
    if (Stars.w>=1.0) light+=caustic(p.zxy*1.81-3.2,t*.43)*(.05+.025*Stars.w);
    if (Stars.w>=3.0) light+=caustic(p.yxz*2.17+1.8,-t*.31)*.045;
    float depth=.7+.3*skyNoise(d*2.5+vec3(t*.08));
    vec3 color=Primary.rgb*depth + Secondary.rgb*pow(light,1.55)*Effect.y*.7;
    finishSky(color,d);
}
