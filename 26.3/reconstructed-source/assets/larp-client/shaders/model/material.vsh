#version 330
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};
layout(std140) uniform Projection { mat4 ProjMat; };
in vec3 Position;
in vec2 UV0;
in ivec2 UV2;
in ivec2 UV1;
in vec4 Color;
in vec3 Normal;
out vec2 texCoord0;
out vec4 vertexColor;
out float hue;
out float surfaceLight;
#if defined(LIQUID_GLASS) || defined(CHAMS_STYLE)
out vec3 viewPosition;
out vec3 viewNormal;
#endif
#ifdef LIQUID_GLASS
out vec3 environmentColor;
out vec2 faceCoord;
#endif
#ifdef CHAMS_STYLE
out vec3 secondaryColor;
out float shading;
out float patternScale;
#endif
void main(){
    gl_Position=ProjMat*ModelViewMat*vec4(Position,1.0);
#if defined(LIQUID_GLASS) || defined(CHAMS_STYLE)
    viewPosition = (ModelViewMat * vec4(Position, 1.0)).xyz;
    viewNormal = normalize(mat3(ModelViewMat) * Normal);
#endif
#ifdef CHAMS_STYLE
    // MaterialConsumer packs the secondary color and shading into the overlay slot.
    int secondaryRG = UV1.x & 65535;
    secondaryColor = vec3(float((secondaryRG >> 8) & 255), float(secondaryRG & 255), float(UV1.y & 255)) / 255.0;
    shading = float((UV1.y >> 8) & 255) / 255.0;
    patternScale = max(float(UV2.y & 65535) / 256.0, .05);
#endif
#ifdef LIQUID_GLASS
    faceCoord = vec2(float(UV2.y & 1), float((UV2.y >> 1) & 1));
    int rg = UV1.x & 65535;
    environmentColor = vec3(float((rg >> 8) & 255), float(rg & 255), float(UV1.y & 255)) / 255.0;
#endif
    texCoord0=UV0;
    vertexColor=Color;
    hue=float(UV2.x)/4096.0;
    vec3 n=normalize(Normal);
    surfaceLight=.58+.3*max(dot(n,normalize(vec3(-.3,.85,.45))),0.0)
                       +.12*max(dot(n,normalize(vec3(.8,.2,-.6))),0.0);
}
