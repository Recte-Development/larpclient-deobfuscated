#version 330
uniform sampler2D History;
uniform sampler2D Depth;
in vec2 texCoord;
out vec4 fragColor;
void main(){
    // Keep the freshly drawn hand already in the scene.
    if(texture(Depth,texCoord).r<1.0)discard;
    fragColor=texture(History,texCoord);
}
