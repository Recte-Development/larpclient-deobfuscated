#version 330
layout(std140) uniform LarpMaterial {
    vec4 WindTime;
    vec4 Water;
    vec4 WaterFlags;
    vec4 Wet;
    vec4 Modes;
    vec4 Sun;
    vec4 Sky;
    vec4 Camera;
    vec4 Snow;
    mat4 ViewProjection;
    mat4 InverseViewProjection;
    vec4 SnowTrailInfo;
    vec4 SnowTrailBounds;
    vec4 SnowTracks[48];
};


layout(std140) uniform DropletBatch { vec4 Drops[512]; vec4 CameraRight; vec4 CameraUp; vec4 SurfaceSize; };
flat out vec4 droplet;
void main() {
    const vec2 corners[6]=vec2[](vec2(-1,-1),vec2(1,-1),vec2(1,1),vec2(-1,-1),vec2(1,1),vec2(-1,1));
    droplet=Drops[gl_VertexID / 6];
    vec2 corner=corners[gl_VertexID % 6];
    vec3 p=droplet.xyz+(CameraRight.xyz*corner.x+CameraUp.xyz*corner.y)*droplet.w*1.6;
    gl_Position=ViewProjection*vec4(p,1);
    if ((ViewProjection*vec4(droplet.xyz,1)).w<.001) gl_Position=vec4(2,2,2,1);
}
