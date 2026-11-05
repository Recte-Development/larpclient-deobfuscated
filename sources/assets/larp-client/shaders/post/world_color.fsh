#version 330
uniform sampler2D Source;
layout(std140) uniform Grade {vec4 Controls;vec4 Tint;vec4 Shadows;vec4 Highlights;vec4 Impact;vec4 Pixel;};
in vec2 texCoord;
out vec4 fragColor;
void main(){
    vec2 p=texCoord*2.0-1.0;
    vec2 offset=p*Pixel.xy*Impact.a*.65;
    vec4 source=texture(Source,texCoord);
    vec3 c=vec3(texture(Source,texCoord+offset).r,source.g,texture(Source,texCoord-offset).b);
    float l=dot(c,vec3(.2126,.7152,.0722));
    c=mix(vec3(l),c,Controls.x)*exp2(Controls.z+Impact.a*.065);
    c=(c-.5)*Controls.y+.5;
    c=max(c,vec3(0))*Tint.rgb*mix(Shadows.rgb,Highlights.rgb,smoothstep(.1,.9,l));
    c=pow(c,vec3(1.0/Controls.w));
    float edge=smoothstep(.25,1.6,dot(p,p));
    c*=1.0-edge*Tint.a;
    c+=Impact.rgb*Impact.a*(edge*.026+exp(-dot(p,p)*5.0)*.012);
    fragColor=vec4(clamp(c,0.0,1.0),source.a);
}
