#version 150
uniform sampler2D Sky;
uniform sampler2D Depth;
in vec2 texCoord;
out vec4 fragColor;
// The client sky, drawn alone, is kept only where the finished scene shows sky: depth is cleared to 1.0
// and anything drawn (terrain, entities, clouds) writes less. Blended under the effects layer.
void main(){
    if(texture(Depth,texCoord).r<1.0)discard;
    fragColor=texture(Sky,texCoord);
}
