#version 330
layout(std140) uniform MenuParameters { vec4 View; vec4 Pointer; };
out vec4 fragColor;
#define size View.xy
#define time View.z
#define pointer Pointer.xy

 float hash(vec2 p){return fract(sin(dot(p,vec2(127.1,311.7)))*43758.5453);}
 float noise(vec2 p){vec2 i=floor(p),f=fract(p);f=f*f*(3.-2.*f);return mix(mix(hash(i),hash(i+vec2(1,0)),f.x),mix(hash(i+vec2(0,1)),hash(i+vec2(1,1)),f.x),f.y);}
 float fbm(vec2 p){float v=0.,a=.5;for(int i=0;i<5;i++){v+=a*noise(p);p=mat2(.8,-.6,.6,.8)*p*2.03+2.1;a*=.5;}return v;}
 void main(){
 vec2 uv=gl_FragCoord.xy/size;vec2 p=(gl_FragCoord.xy-.5*size)/size.y*3.0+pointer*.09;
 float t=time*.025;
 vec2 q=vec2(fbm(p+vec2(t,-t*.4)),fbm(p+vec2(4.2,-t*.7)));
 vec2 r=vec2(fbm(p+3.1*q+vec2(1.7,t*.6)),fbm(p+3.3*q+vec2(8.3,-t*.45)));
 float f=fbm(p+3.6*r);
 float band=exp(-pow((p.y-.42*sin(p.x*1.4+t)-.12)*.9,2.));
 float mist=smoothstep(.25,.78,f)*band;
 float wisps=pow(max(0.,1.-abs(f-.53)*5.2),5.)*smoothstep(.34,.8,r.x)*band;
 vec3 col=vec3(.006,.018,.04);
 col+=vec3(.025,.12,.27)*mist;
 col+=vec3(.07,.30,.43)*wisps*.72;
 col+=vec3(.025,.08,.17)*pow(q.x,3.);
 float shade=mix(.62,1.,smoothstep(.05,.8,abs(uv.x-.46)*1.7));
 col*=shade;
 // The homepage displays the nebula canvas at 47% over #0a0e14.
 col=mix(vec3(10.,14.,20.)/255.,col,.47);
 fragColor=vec4(col,1.);
 }