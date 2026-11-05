#version 150
uniform sampler2D Source;
in vec2 texCoord;
out vec4 fragColor;
// The blurred scene goes under client UI in the hidden layer. The main target's alpha is not coverage,
// so it is drawn fully opaque; otherwise the sharp world would show through the blur on screen.
void main(){
    fragColor=vec4(texture(Source,texCoord).rgb,1.0);
}
