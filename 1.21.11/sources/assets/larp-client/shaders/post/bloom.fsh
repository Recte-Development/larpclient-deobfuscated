#version 330
uniform sampler2D Source;
layout(std140) uniform PostParameters { vec4 Parameters; };
in vec2 texCoord;
out vec4 fragColor;
void main(){vec4 c=texture(Source,texCoord);fragColor=vec4(c.rgb*Parameters.x,0.0);}
