#version 330
uniform sampler2D Source;
layout(std140) uniform PostParameters { vec4 Parameters; };
in vec2 texCoord;
out vec4 fragColor;
void main(){
    vec2 stepUV=vec2(0.0,Parameters.w);
    vec4 color=texture(Source,texCoord)*0.227027;
    color+=(texture(Source,texCoord+stepUV*1.384615)+texture(Source,texCoord-stepUV*1.384615))*0.316216;
    color+=(texture(Source,texCoord+stepUV*3.230769)+texture(Source,texCoord-stepUV*3.230769))*0.070270;
    fragColor=color;
}
