#version 150
uniform sampler2D Interface;
uniform sampler2D Effects;
in vec2 texCoord;
out vec4 fragColor;
// Both layers hold premultiplied color. The interface goes over the effects. Rows are flipped so
// the read-back starts at the top, and red and blue swap so its bytes are a Windows BGRA DIB.
void main(){
    vec2 uv=vec2(texCoord.x,1.0-texCoord.y);
    vec4 ui=texture(Interface,uv);
    vec4 fx=texture(Effects,uv);
    vec4 c=ui+fx*(1.0-ui.a);
    // Glow is added as colour without alpha. Windows layered windows need valid premultiplied pixels
    // (colour <= alpha), so alpha is raised to the brightest channel; the glow stays visible.
    c.a=max(c.a,max(c.r,max(c.g,c.b)));
    fragColor=clamp(c,0.0,1.0).bgra;
}
