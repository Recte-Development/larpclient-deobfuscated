#version 150
uniform sampler2D Scene;
uniform sampler2D Depth;
in vec2 texCoord;
out vec4 fragColor;
// Right after the captured scene's vanilla hand, the main depth holds only that hand (vanilla clears depth to
// 1.0, standard OpenGL depth, before drawing it). There the scene from just before the hand is laid under the hidden layer,
// so the player sees through the vanilla hand to the world; their own hand is then drawn into the layer.
void main(){
    if(texture(Depth,texCoord).r>=1.0)discard;
    fragColor=vec4(texture(Scene,texCoord).rgb,1.0);
}
