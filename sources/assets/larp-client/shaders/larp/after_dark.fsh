#version 330
// Original After Dark material. World rays come from the existing sky triangle.
in vec3 skyDirection;
layout(std140) uniform SkyPalette {
    vec4 LarpTimeResolution; vec4 LarpColorAlpha; vec4 LarpMotion; vec4 LarpCamera;
    vec4 LarpUnused0; vec4 LarpUnused1; vec4 LarpUnused2; vec4 LarpUnused3; vec4 LarpUnused4; vec4 LarpUnused5;
};
out vec4 fragColor;
float hash(vec3 p) { return fract(sin(dot(p,vec3(127.1,311.7,74.7)))*43758.5453); }
float noise3(vec3 p) {
    vec3 i=floor(p), f=fract(p); f=f*f*(3.0-2.0*f);
    return mix(mix(mix(hash(i),hash(i+vec3(1,0,0)),f.x),
                   mix(hash(i+vec3(0,1,0)),hash(i+vec3(1,1,0)),f.x),f.y),
               mix(mix(hash(i+vec3(0,0,1)),hash(i+vec3(1,0,1)),f.x),
                   mix(hash(i+vec3(0,1,1)),hash(i+vec3(1,1,1)),f.x),f.y),f.z);
}
float fbm(vec3 p) {
    float v=0.0,w=.55;
    for(int i=0;i<4;i++) {
        if(float(i)>=LarpMotion.z) break;
        v+=noise3(p)*w; p=p*2.03+vec3(7.1,13.3,4.8); w*=.48;
    }
    return v;
}
float capsule(vec2 p,vec2 a,vec2 b,float radius) {
    vec2 line=b-a;
    return length(p-a-line*clamp(dot(p-a,line)/dot(line,line),0.0,1.0))-radius;
}
float triangle(vec2 p,vec2 a,vec2 b,vec2 c) {
    vec2 e0=b-a,e1=c-b,e2=a-c,v0=p-a,v1=p-b,v2=p-c;
    vec2 q0=v0-e0*clamp(dot(v0,e0)/dot(e0,e0),0.0,1.0);
    vec2 q1=v1-e1*clamp(dot(v1,e1)/dot(e1,e1),0.0,1.0);
    vec2 q2=v2-e2*clamp(dot(v2,e2)/dot(e2,e2),0.0,1.0);
    float s=sign(e0.x*e2.y-e0.y*e2.x);
    vec2 edge=min(min(vec2(dot(q0,q0),s*(v0.x*e0.y-v0.y*e0.x)),
                      vec2(dot(q1,q1),s*(v1.x*e1.y-v1.y*e1.x))),
                      vec2(dot(q2,q2),s*(v2.x*e2.y-v2.y*e2.x)));
    return -sqrt(edge.x)*sign(edge.y);
}
float witch(vec2 p,float t) {
    // Original side silhouette, in angular sky coordinates. One rider, no texture or extra pass.
    float flutter=sin(t*2.4)*.055;
    float shape=capsule(p,vec2(-1.12,-.31),vec2(1.10,-.20),.025);
    shape=min(shape,triangle(p,vec2(-.83,-.26),vec2(-1.38,-.12+flutter),vec2(-1.32,-.52+flutter)));
    shape=min(shape,capsule(p,vec2(-1.29,-.25+flutter),vec2(-.85,-.29),.028));
    shape=min(shape,capsule(p,vec2(-1.28,-.40+flutter),vec2(-.85,-.30),.028));
    // Seated cloak, bent knee, boot and arms reaching the broom.
    shape=min(shape,triangle(p,vec2(-.16,.26),vec2(-.67,-.20+flutter),vec2(.27,-.22)));
    shape=min(shape,capsule(p,vec2(-.08,.16),vec2(.22,-.12),.12));
    shape=min(shape,capsule(p,vec2(.17,-.14),vec2(.49,-.31),.075));
    shape=min(shape,capsule(p,vec2(.49,-.31),vec2(.36,-.63),.058));
    shape=min(shape,triangle(p,vec2(.31,-.58),vec2(.36,-.69),vec2(.61,-.69)));
    shape=min(shape,capsule(p,vec2(-.02,.22),vec2(.40,.02),.052));
    shape=min(shape,capsule(p,vec2(.40,.02),vec2(.61,-.24),.043));
    // Chin, hooked nose, windblown hair and a crooked pointed hat.
    shape=min(shape,length((p-vec2(.02,.45))/vec2(.15,.19))*.15-.15);
    shape=min(shape,triangle(p,vec2(.12,.49),vec2(.29,.40),vec2(.12,.36)));
    shape=min(shape,triangle(p,vec2(-.09,.51),vec2(-.45,.25+flutter),vec2(-.04,.29)));
    shape=min(shape,capsule(p,vec2(-.32,.61),vec2(.30,.58),.035));
    shape=min(shape,triangle(p,vec2(-.23,.61),vec2(.19,.61),vec2(-.06,1.02)));
    shape=min(shape,triangle(p,vec2(-.06,1.02),vec2(-.14,.83),vec2(-.40,.91)));
    return shape;
}
void main() {
    vec3 d=normalize(skyDirection);
    vec3 amber=vec3(.95,.43,.12), midnight=vec3(.035,.018,.026);
    // Preserve the native fog seam; the sky above it carries the Halloween ember haze.
    vec3 color=mix(LarpColorAlpha.rgb,midnight,smoothstep(-.035,.14,d.y));
    color+=vec3(.060,.020,.006)*exp(-pow((d.y-.18)*2.5,2.0))*smoothstep(.01,.12,d.y);
    const vec3 moonDir=vec3(-.451,.561,.694);
    vec3 m=normalize(moonDir);
    vec3 right=normalize(cross(vec3(0,1,0),m)), up=cross(m,right);
    vec2 uv=vec2(dot(d,right),dot(d,up))/.155;
    float r=length(uv), alignment=max(0.0,dot(d,m));
    float halo=exp(-max(0.0,r-1.0)*3.8)*.065*LarpMotion.y*step(0.0,dot(d,m));
    color+=amber*halo;
    if(r<1.015 && alignment>.9) {
        float edge=1.0-smoothstep(.985,1.015,r);
        vec3 n=vec3(uv,sqrt(max(0.0,1.0-dot(uv,uv))));
        float maria=fbm(n*5.7+vec3(3,7,1));
        float surface=.52+.38*maria+.10*noise3(n*43.0);
        // Bounded craters: raised rims and shaded bowls, stable in lunar coordinates.
        for(int i=0;i<8;i++) {
            float k=float(i);
            vec2 p=vec2(sin(k*7.31),cos(k*4.17))*.74;
            float radius=.07+fract(k*.371)*.13, cr=length(uv-p)/radius;
            surface-=.17*(1.0-smoothstep(.0,.85,cr));
            surface+=.12*exp(-pow((cr-.92)*9.0,2.0));
        }
        float diffuse=.24+.76*max(0.0,dot(n,normalize(vec3(-.55,.35,1))));
        vec3 moon=amber*surface*diffuse*(.78+.22*LarpMotion.x);
        color=mix(color,moon,edge);
    }
    // Continuous 3D sky noise: no longitude seam, wrap or time modulo.
    float t=LarpTimeResolution.x;
    float clouds=fbm(d*5.8+vec3(t*.006,0,t*.002));
    float mist=smoothstep(.30,.65,clouds)*smoothstep(.03,.18,d.y);
    vec3 cloudColor=mix(vec3(.14,.035,.009),vec3(.33,.095,.018),smoothstep(.32,.68,clouds));
    color=mix(color,cloudColor+amber*halo*.8,mist*.74);
    vec3 cell=floor(d*260.0), f=fract(d*260.0)-.5;
    float stars=step(.9983,hash(cell))*exp(-dot(f,f)*100.0)*.14;
    color+=vec3(.65,.35,.14)*stars*smoothstep(.15,.6,d.y)*(1.0-mist)*smoothstep(1.02,1.2,r);
    // A continuous figure-eight flight in the moon's world-fixed tangent plane.
    // Foreshortening turns the rider at the ends without a visible facing flip or wrap.
    float phase=t*.035-.8, heading=cos(phase);
    vec2 flight=vec2(.58*sin(phase),.075*sin(phase*2.0)+.02);
    vec2 p=(vec2(dot(d,right),dot(d,up))-flight)/.068;
    float turn=max(.001,abs(heading));
    p.x/=heading<0.0 ? -turn : turn;
    float mask=0.0;
    if(dot(d,m)>.65 && abs(p.x)<1.6 && abs(p.y)<1.2) {
        float edge=witch(p,t);
        float aa=max(fwidth(edge),.002);
        mask=1.0-smoothstep(-aa,aa,edge);
    }
    color=mix(color,vec3(.009,.005,.012),mask*.97);
    fragColor=vec4(max(color,vec3(0)),1.0);
}
