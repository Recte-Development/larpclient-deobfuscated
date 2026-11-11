#version 330
uniform sampler2D Source;
uniform sampler2D History;
uniform sampler2D Depth;
layout(std140) uniform MotionBlur {mat4 Reproject;vec4 Lens;vec4 Pixel;};
in vec2 texCoord;
out vec4 fragColor;
void main(){
    vec4 current=texture(Source,texCoord);
    // Hand pixels (written reversed depth) never enter the history, except to seed it.
    if(texture(Depth,texCoord).r>0.0&&Pixel.w<.5){fragColor=texture(History,texCoord);return;}
    fragColor=mix(current,texture(History,texCoord),Pixel.z);
}
