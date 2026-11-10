#version 150
uniform sampler2D Interface;
in vec2 texCoord;
out vec4 fragColor;
// Premultiplied interface only. Rows are flipped so the read-back starts at the top, and red and
// blue swap so its bytes are a Windows BGRA DIB.
void main(){
    vec4 c=texture(Interface,vec2(texCoord.x,1.0-texCoord.y));
    // Windows layered windows need valid premultiplied pixels (colour <= alpha); additive UI glow keeps showing.
    c.a=max(c.a,max(c.r,max(c.g,c.b)));
    fragColor=clamp(c,0.0,1.0).bgra;
}
