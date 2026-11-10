#version 330
uniform sampler2D Source;
uniform sampler2D Mask;
layout(std140) uniform Halo { vec4 Color;vec4 Parameters; };
in vec2 texCoord;
out vec4 fragColor;
void main(){
    float center=texture(Mask,texCoord).a;
    bool hasHalo=Parameters.y>0.0;
    float edge=center,blur=0.0;
    if(hasHalo) blur=texture(Source,texCoord).a;
    for(int i=0;i<16;i++){
        float a=float(i)*6.2831853/16.0;vec2 d=vec2(cos(a),sin(a))*Parameters.zw*Parameters.x;
        edge=max(edge,texture(Mask,texCoord+d).a);
        if(hasHalo) blur+=texture(Source,texCoord+d*.65).a*.0625;
    }
    float border=max(0.0,edge-center)*Color.a;
    float halo=max(0.0,blur*.5-center)*Parameters.y;
    fragColor=vec4(Color.rgb,clamp(border+halo,0.0,1.0));
}
