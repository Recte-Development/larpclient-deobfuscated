#version 330
uniform sampler2D Sampler0;
in vec2 texCoord0;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec2 p=texCoord0*2.0-1.0;
    float r=length(p), aa=max(length(fwidth(p)),.012);
    float sector=abs(mod(atan(p.y,p.x)+.523599,1.047198)-.523599);
    vec2 q=vec2(cos(sector),sin(sector))*r;
    float spoke=1.0-smoothstep(.027,.027+aa,q.y);
    float branch=1.0-smoothstep(.023,.023+aa,abs(q.y-abs(q.x-.43)*.65));
    branch*=smoothstep(.2,.27,q.x)*(1.0-smoothstep(.57,.64,q.x));
    float crystal=max(spoke,branch)*(1.0-smoothstep(.74,.88,r));
    // The existing soft disk provides a powder core when distant crystals become subpixel.
    float powder=texture(Sampler0,vec2((1.0+texCoord0.x)/9.0,texCoord0.y)).a;
    float detail=1.0-smoothstep(.035,.16,aa);
    float alpha=mix(powder*.8,max(crystal*.65,powder*.48),detail)*vertexColor.a;
    if(alpha<.002)discard;
    fragColor=vec4(vertexColor.rgb,alpha);
}
